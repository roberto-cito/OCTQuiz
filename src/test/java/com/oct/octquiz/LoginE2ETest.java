package com.oct.octquiz;

import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.User.UserRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoginE2ETest {

    @LocalServerPort
    private int port;

    // Recupera il context-path dalle properties (default a stringa vuota se non impostato)
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        // Costruisce la base URL dinamica: es. http://localhost:12345/octquiz
        baseUrl = "http://localhost:" + port + contextPath;

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);

        if (userRepository.findByEmail("test@example.com").isEmpty()) {
            UserEntity user = new UserEntity();
            user.setEmail("test@example.com");
            user.setNome("Test");
            user.setCognome("User");
            user.setHash_password(passwordEncoder.encode("password"));
            user.setRuolo("USER");
            user.setCategorie(new HashSet<>());
            userRepository.save(user);
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void login_shouldRedirectToHome_whenCredentialsAreValid() {
        // Naviga alla root del contesto
        driver.get(baseUrl + "/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@onclick=\"openModal('login')\"]"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("test@example.com");
        driver.findElement(By.id("password")).sendKeys("password");

        // Submit (Nota: l'action della form dovrebbe essere relativa o includere il context path)
        driver.findElement(By.cssSelector("form[action*='/login'] button[type='submit']")).click();

        // Verifica Redirect includendo il context path
        String expectedHomeUrl = contextPath + "/home";
        wait.until(ExpectedConditions.urlContains(expectedHomeUrl));
        assertTrue(driver.getCurrentUrl().contains(expectedHomeUrl), "Should redirect to " + expectedHomeUrl);
    }

    @Test
    void login_shouldShowError_whenCredentialsAreInvalid() {
        driver.get(baseUrl + "/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@onclick=\"openModal('login')\"]"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys("test@example.com");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");

        driver.findElement(By.cssSelector("form[action*='/login'] button[type='submit']")).click();

        // Aspetta il parametro error nell'URL
        wait.until(ExpectedConditions.urlContains("error=true"));
        assertTrue(driver.getCurrentUrl().contains(contextPath + "/"), "Should be on base context path");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginModal")));

        boolean isErrorVisible = driver.findElement(By.cssSelector(".error-message")).isDisplayed();
        assertTrue(isErrorVisible, "Error message should be displayed");
    }
}