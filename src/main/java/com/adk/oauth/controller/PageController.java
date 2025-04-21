package com.adk.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/unauthorized")
    public String unauthorizedPage() {
        return "unauthorized";
    }
}