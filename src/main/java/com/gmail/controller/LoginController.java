package com.gmail.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Objects;

@Controller
public class LoginController {

//    @GetMapping("/login")
//    public String login(@AuthenticationPrincipal OAuth2User principal, Model model) {
//        if (Objects.nonNull(principal)) {
//            model.addAttribute("username", principal.getAttribute("name"));
//        }
//        return "login";
//    }
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal OAuth2User user) {
        // If user is already authenticated, redirect to welcome page
        if (user != null) {
            return "redirect:/welcome";
        }
        // If user is not authenticated, show login page
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal OAuth2User user, Model model) {
        if (user != null) {
            model.addAttribute("username", user.getAttribute("name"));
        }
        return "welcome keycloak google login";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
