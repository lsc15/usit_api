package com.usit.app.spring.security.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UserDetails extends org.springframework.security.core.userdetails.User {
  /**
     *
     */
    private static final long serialVersionUID = -2009085498606833622L;
    private SignedMember user;
    private List<String> roles;

    public UserDetails(SignedMember user, List<String> roles) {
        super(user.getUsername(), user.getPassword(), roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        this.user = user;
        this.roles = roles;
    }
}
