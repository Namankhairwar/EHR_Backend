package com.clinic.patient.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Krishana dubey
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtServic) {
        this.jwtService = jwtServic;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContextHolder.getContext().getAuthentication();
        String path = request.getServletPath();
        if(path.contains("auth")){
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println("JWT is authenticating");
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer ")){
            log.warn("JWT not available");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication required please login");
            return;
        }
        String token = header.substring(7);
        System.out.println(jwtService.validateToken(token));
        if(!jwtService.validateToken(token)){
            log.warn("Invalid Token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or Expired token");
            return;
        }
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new   UsernamePasswordAuthenticationToken(request.getHeader("email"),null,new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request,response);
    }

}
