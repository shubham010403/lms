package com.example.lms.exception;

public class CustomServiceException extends RuntimeException{
    public CustomServiceException(String message){
        super(message);
    }
}