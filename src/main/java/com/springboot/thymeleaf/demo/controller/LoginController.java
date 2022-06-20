package com.springboot.thymeleaf.demo.controller;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {


    @GetMapping(value = {"/","/showMyLoginPage"})
    public String showMyLoginPage() {

        return "fancy-login";

    }
}
