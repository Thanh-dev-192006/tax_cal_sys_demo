package com.oop.project.exception;

public class InvalidCredentialException extends Exception {
    public InvalidCredentialException(String message) {
        super(message);
    }
}