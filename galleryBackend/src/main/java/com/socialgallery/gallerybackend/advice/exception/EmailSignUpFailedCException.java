package com.socialgallery.gallerybackend.advice.exception;

public class EmailSignUpFailedCException extends RuntimeException{
    public EmailSignUpFailedCException() {
        super();
    }

    public EmailSignUpFailedCException(String message) {
        super(message);
    }

    public EmailSignUpFailedCException(String message, Throwable cause) {
        super(message, cause);
    }
}
