package com.hr.demo.service.faceverify;

/**
 * Raised when the face-verification microservice rejects a request or is unreachable.
 */
public class FaceVerificationException extends RuntimeException {
    public FaceVerificationException(String message) {
        super(message);
    }

    public FaceVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
