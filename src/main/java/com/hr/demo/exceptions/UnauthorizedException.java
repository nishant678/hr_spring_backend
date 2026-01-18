package com.hr.demo.exceptions;

import com.hr.demo.exceptions.ApiException;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
