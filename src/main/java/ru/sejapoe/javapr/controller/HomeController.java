package ru.sejapoe.javapr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
//    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/home")
    public String home() {
        return "index";
    }
}
