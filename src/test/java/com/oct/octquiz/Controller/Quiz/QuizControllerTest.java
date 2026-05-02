package com.oct.octquiz.Controller.Quiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import com.oct.octquiz.Model.Categoria.CategoryService;
import com.oct.octquiz.Model.Domanda.DomandaEntity;
import com.oct.octquiz.Model.Domanda.DomandaService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import com.oct.octquiz.Model.User.PasswordUtility;
import com.oct.octquiz.Model.User.UserEntity;
import com.oct.octquiz.Model.Categoria.Stat.StatCategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private DomandaService domandaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private StatCategoriaService statCategoriaService;

    private CategoriaEntity testCategory;
    private UserEntity testUser;
    private List<DomandaEntity> testQuestions;

    @BeforeEach
    void setUp() {
        testCategory = new CategoriaEntity(1, "Test Category", 30, true);
        testUser = new UserEntity();
        testUser.setEmail("test@example.com");
        testUser.setHash_password(PasswordUtility.hashPassword("password"));
        testUser.setNome("Test");
        testUser.setCognome("User");
        testUser.setRuolo("USER");
        testUser.setCategorie(new HashSet<>());

        testQuestions = new ArrayList<>();
        testQuestions.add(new DomandaEntity(1, testCategory, "Question 1", null, null, "A1", "A2", "A3", "A4", 1));
        testQuestions.add(new DomandaEntity(2, testCategory, "Question 2", null, null, "B1", "B2", "B3", "B4", 2));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testReturnInstruction() throws Exception {
        when(categoryService.findById(1)).thenReturn(testCategory);
        when(customUserDetailsService.findByEmail("test@example.com")).thenReturn(testUser);

        mockMvc.perform(get("/quiz").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz/prequiz"))
                .andExpect(model().attributeExists("categoria"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testStartQuiz() throws Exception {
        when(categoryService.findById(1)).thenReturn(testCategory);
        when(domandaService.findAllByCategoria(testCategory)).thenReturn(testQuestions);
        when(customUserDetailsService.findByEmail("test@example.com")).thenReturn(testUser);

        mockMvc.perform(post("/quiz").param("id", "1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz/quiz"))
                .andExpect(model().attribute("domande", testQuestions));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testSubmitQuizAllCorrect() throws Exception {
        when(categoryService.findById(1)).thenReturn(testCategory);
        when(domandaService.findAllByCategoria(testCategory)).thenReturn(testQuestions);
        when(customUserDetailsService.findByEmail("test@example.com")).thenReturn(testUser);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> answers = new HashMap<>();
        answers.put("1", 1);
        answers.put("2", 2);
        String answersJson = mapper.writeValueAsString(answers);

        // Expected score: 8 + 8 = 16

        mockMvc.perform(post("/quiz/submit")
                        .param("categoriaId", "1")
                        .param("answers", answersJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz/result"))
                .andExpect(model().attribute("punteggio", 16))
                .andExpect(model().attribute("totale", 16));
        
        verify(customUserDetailsService).addCategory(any(CategoriaEntity.class), any(String.class));
    }
    
    @Test
    @WithMockUser(username = "test@example.com")
    void testSubmitQuizOneWrong() throws Exception {
        when(categoryService.findById(1)).thenReturn(testCategory);
        when(domandaService.findAllByCategoria(testCategory)).thenReturn(testQuestions);
        when(customUserDetailsService.findByEmail("test@example.com")).thenReturn(testUser);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> answers = new HashMap<>();
        answers.put("1", 1); // Correct
        answers.put("2", 3); // Wrong
        String answersJson = mapper.writeValueAsString(answers);

        // Expected score: 8 - 2 = 6

        mockMvc.perform(post("/quiz/submit")
                        .param("categoriaId", "1")
                        .param("answers", answersJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz/result"))
                .andExpect(model().attribute("punteggio", 6));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void testSubmitQuizUnanswered() throws Exception {
        when(categoryService.findById(1)).thenReturn(testCategory);
        when(domandaService.findAllByCategoria(testCategory)).thenReturn(testQuestions);
        when(customUserDetailsService.findByEmail("test@example.com")).thenReturn(testUser);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> answers = new HashMap<>();
        answers.put("1", 1); // Correct
        // Question 2 is unanswered
        String answersJson = mapper.writeValueAsString(answers);

        // Expected score: 8 + 0 = 8

        mockMvc.perform(post("/quiz/submit")
                        .param("categoriaId", "1")
                        .param("answers", answersJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("quiz/result"))
                .andExpect(model().attribute("punteggio", 8));
    }
}
