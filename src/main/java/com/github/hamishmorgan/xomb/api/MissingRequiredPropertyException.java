package com.github.hamishmorgan.xomb.api;

/**
 * Created by hamish on 13/09/14.
 */
public class MissingRequiredPropertyException extends RuntimeException {

    private final String required;

    public MissingRequiredPropertyException(String required) {
        this.required = required;
    }
}
