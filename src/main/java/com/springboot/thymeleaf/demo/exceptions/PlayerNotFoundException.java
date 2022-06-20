package com.springboot.thymeleaf.demo.exceptions;

public class PlayerNotFoundException extends RuntimeException{
    public PlayerNotFoundException(String message){
        printStackTrace();
    }
}
