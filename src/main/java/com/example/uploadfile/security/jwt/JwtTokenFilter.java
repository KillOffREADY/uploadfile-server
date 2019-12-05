package com.example.uploadfile.security.jwt;

import com.example.uploadfile.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider provider;
    @Autowired
    private UserDetailsServiceImpl service;

//    @Autowired
//    public JwtTokenFilter(JwtTokenProvider provider, UserDetailsServiceImpl service) {
//        this.provider = provider;
//        this.service = service;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
        String jwtToken = getJwtToken(httpServletRequest);
        if (getJwtToken(httpServletRequest)!= null && provider.isJwtTokenValid(jwtToken)) {
            String username = provider.getUsernameFromJwtToken(jwtToken);
            UserDetails userPrincipal = service.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        } catch (Exception e) {
            System.out.println("Can NOT set user authentication -> Message: {}" + e.getMessage());
            e.printStackTrace();
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        } else {
//            throw new JwtException("Bad authHeader -> " + authHeader);
            return null;
        }
    }
}
