package com.example.imprint.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/loginForm")
    public String loginForm() {
        // src/main/resources/templates/loginForm.html을 찾아감
        return "loginForm";
    }
}