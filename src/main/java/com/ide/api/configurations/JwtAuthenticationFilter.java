package com.ide.api.configurations;

import com.ide.api.service.CustomUserDetailsService;
import com.ide.api.service.CustomUserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private  JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws  ServletException, IOException{
        try{
        String jwt = parseJwt(request);
        if(jwt != null && jwtTokenProvider.validateJwtToken(jwt)){
            String username = jwtTokenProvider.getUserNameFromJwtToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else if(jwt != null && !jwtTokenProvider.validateJwtToken(jwt)){
            ResponseCookie cookie = jwtTokenProvider.deleteJwtCookie();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expiré ou invalidé");
            return;
        }
    }catch (Exception e){
        logger.error("Impossible de définir l'authentification de l'utilisateur: {}", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Erreur interne du serveur...");
        return;
    }
        filterChain.doFilter(request, response);
}

    private String parseJwt(HttpServletRequest request){
        String jwt = jwtTokenProvider.getJwtFromCookie(request);
        return jwt;
    }

}
