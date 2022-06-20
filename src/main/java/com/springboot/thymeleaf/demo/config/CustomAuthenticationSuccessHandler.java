package com.springboot.thymeleaf.demo.config;


import com.springboot.thymeleaf.demo.service.CoachService;
import com.springboot.thymeleaf.demo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CoachService coachService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        String userName = authentication.getName();
        String urlHandle="";
        if(playerService.findByUserName(userName)!= null){
            // now place in the session
            session.setAttribute("user", playerService.findByUserName(userName));
            urlHandle = "/playerLanding";
        }else{
            session.setAttribute("user", coachService.findByUserName(userName));
            urlHandle = "/coachLanding";
        }
        response.sendRedirect(request.getContextPath() +urlHandle);
        // forward to home.html page

    }

}
