package com.nethum.springsecuirtydemo.config;

import com.nethum.springsecuirtydemo.services.JwtService;
import com.nethum.springsecuirtydemo.services.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");     //get the request and authorization
        String token = null;
        String userName = null;

        if(authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);    //extract the token
            userName = jwtService.extractUserName(token);   // extract the username
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails = applicationContext.getBean(MyUserDetailService.class).loadUserByUsername(userName);

            if(jwtService.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}
