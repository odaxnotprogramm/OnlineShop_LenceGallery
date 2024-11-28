package org.web.shop.app.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.web.shop.app.app.model.User;
import org.web.shop.app.app.service.impl.UserService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "logIn";
    }

    @GetMapping("/register")
    public String register() {
        return "registration";
    }

    @PostMapping("/register")
    public String register(User user) {
        userService.createUser(user);
        return "redirect:/login";
    }

//    @GetMapping("/")
//    public String root() {
//        return "redirect:/login";
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            request.getSession().invalidate();
//        }
//        return "redirect:/";
//    }

}