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
        
        System.out.println("Filtro JWT ejecutado para: " + request.getRequestURI());

        String token = getJwtFromRequest(request);

        if (StringUtils.hasText(token)) {
            System.out.println("🔍 Token recibido, validando...");
            boolean isValid = TokenJWTUtility.validateToken(token);
            System.out.println("🔍 Token válido: " + isValid);
            
            if (isValid) {
                String username = TokenJWTUtility.getUsernameFromToken(token);
                System.out.println("🔍 Username extraído: " + username);
                
                User user = userService.findByUsername(username);
                System.out.println("🔍 Usuario encontrado: " + (user != null));

                if (user != null) {
                    System.out.println("🔍 Creando autenticación para usuario: " + user.getUsername());
                    
                    // Crear UserDetails simple
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
                    System.out.println("🔍 Autenticación establecida: " + (SecurityContextHolder.getContext().getAuthentication() != null));
                    System.out.println("🔍 Principal actual: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().getSimpleName());
                }
            }
        } else {
            System.out.println("🔍 No se recibió token válido");
        }

        System.out.println("JWT recibido: " + token);

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