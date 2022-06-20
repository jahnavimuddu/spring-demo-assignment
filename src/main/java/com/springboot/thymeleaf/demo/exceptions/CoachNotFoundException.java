package com.springboot.thymeleaf.demo.exceptions;

public class CoachNotFoundException extends RuntimeException{
    public CoachNotFoundException(String message){
        printStackTrace();
    }
}
