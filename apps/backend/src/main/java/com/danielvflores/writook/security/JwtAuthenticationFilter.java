package com.danielvflores.writook.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.UserService;
import com.danielvflores.writook.utility.TokenJWTUtility;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, java.io.IOException {

        String token = getJwtFromRequest(request);

        if (StringUtils.hasText(token)) {
            boolean isValid = TokenJWTUtility.validateToken(token);
            if (isValid) {
                String usernameOrEmail = TokenJWTUtility.getUsernameFromToken(token);
                User user = userService.findByUsername(usernameOrEmail);
                if (user == null) {
                    user = userService.findByEmail(usernameOrEmail);
                }
                if (user != null) {
                    org.springframework.security.core.userdetails.User userDetails = 
                        new org.springframework.security.core.userdetails.User(
                            user.getUsername(), 
                            user.getPassword(), 
                            java.util.Collections.emptyList()
                        );
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, java.util.Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}