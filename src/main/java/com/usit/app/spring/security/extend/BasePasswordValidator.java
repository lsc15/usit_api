package com.usit.app.spring.security.extend;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.usit.app.spring.security.PasswordValidator;

public class BasePasswordValidator implements PasswordValidator {

    private PasswordEncoder passwordEncoder;

    /**
     * <pre>
     * getPasswordEncoder
     * </pre>
     * @param N/A
     * @return PasswordEncoder
     */
    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    /**
     * <pre>
     * setPasswordEncoder
     * </pre>
     * @param PasswordEncoder passwordEncoder
     * @return N/A
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * <pre>
     * validatePassword
     * </pre>
     * @param String storedPass, String inputPass, Object salt
     * @return N/A
     */
    @Override
    public boolean validatePassword(String storedPass, String inputPass, Object salt) {

        return getPasswordEncoder().matches(inputPass, storedPass);
    }

}
