package org.example.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.userservice.service.CustomUserDetailsService;
import org.example.userservice.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        logger.debug("Request URI: {}", request.getRequestURI());
        logger.debug("Authorization Header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Bearer token found, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        String username = null;

        try {
            username = jwtService.getUsernameFromToken(jwt);
            logger.debug("Extracted username from token: {}", username);
        } catch (Exception e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug("Loaded user details for: {}", username);
                logger.debug("User authorities: {}", userDetails.getAuthorities());

                if (jwtService.validateToken(jwt)) {
                    logger.debug("Token is valid for user: {}", username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("Authentication set in SecurityContext for user: {}", username);

                } else {
                    logger.warn("Token validation failed for user: {}", username);
                }
            } catch (Exception e) {
                logger.error("Failed to load user details or validate token: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}