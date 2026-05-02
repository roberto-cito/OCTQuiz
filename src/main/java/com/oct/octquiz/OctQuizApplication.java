package com.oct.octquiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableAsync
public class OctQuizApplication {

    public static void main(String[] args) throws IOException {
        Path path=Paths.get("uploads/");
        if(!Files.exists(path)) {
            Files.createDirectories(path);
        }

        SpringApplication.run(OctQuizApplication.class, args);
    }

}
