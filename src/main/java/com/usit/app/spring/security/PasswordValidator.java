package com.usit.app.spring.security;

public interface PasswordValidator {

    public abstract boolean validatePassword(String paramString1, String paramString2, Object paramObject);
}
