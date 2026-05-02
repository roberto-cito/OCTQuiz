package com.oct.octquiz.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PingController {
    @GetMapping("/ping")
    public Object ping() {
        return ResponseEntity.ok().build();
    }
}
