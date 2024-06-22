package com.ide.api.configurations;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//    }
}
