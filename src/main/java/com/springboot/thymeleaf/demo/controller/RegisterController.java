package com.springboot.thymeleaf.demo.controller;

import com.springboot.thymeleaf.demo.entity.Coach;
import com.springboot.thymeleaf.demo.entity.Player;
import com.springboot.thymeleaf.demo.service.CoachService;
import com.springboot.thymeleaf.demo.service.PlayerService;
import com.springboot.thymeleaf.demo.user.CrmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private static final String CRM_USER = "crmUser";
    private static final String REGISTRATION_FORM = "registration-form";
    private static final String USER_NAME_ALREADY_EXISTS = "User name already exists.";
    @Autowired
    private PlayerService playerService;

    @Autowired
    private CoachService coachService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/showRegistrationForm")
    public String showMyLoginPage(Model theModel) {

        theModel.addAttribute(CRM_USER, new CrmUser());

        return REGISTRATION_FORM;
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute(CRM_USER) CrmUser theCrmUser,
            BindingResult theBindingResult,
            Model theModel) {

        String userName = theCrmUser.getUserName();

        String userType = theCrmUser.getUserType();

        // form validation
        if (theBindingResult.hasErrors()){
            return REGISTRATION_FORM;
        }

        if(userType.equals("Player")) {
            // check the database if user already exists
            Player existing = playerService.findByUserName(userName);
            if (existing != null) {
                theModel.addAttribute(CRM_USER, new CrmUser());
                theModel.addAttribute("registrationError", USER_NAME_ALREADY_EXISTS);

                logger.warning(USER_NAME_ALREADY_EXISTS);
                return REGISTRATION_FORM;
            }

            // create user account
            playerService.saveCrmUser(theCrmUser);

            logger.info("Successfully created user!!");
            return "registration-confirmation";
        } else{

            // check the database if user already exists
            Coach existing = coachService.findByUserName(userName);
            if (existing != null) {
                theModel.addAttribute(CRM_USER, new CrmUser());
                theModel.addAttribute("registrationError", USER_NAME_ALREADY_EXISTS);

                logger.warning(USER_NAME_ALREADY_EXISTS);
                return REGISTRATION_FORM;
            }

            // create user account
            coachService.saveCrmUser(theCrmUser);

            logger.info("Successfully created user!!");
            return "registration-confirmation";

        }


    }
}
