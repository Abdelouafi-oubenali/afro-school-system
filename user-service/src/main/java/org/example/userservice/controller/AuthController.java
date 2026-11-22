package org.example.userservice.controller;

import org.example.userservice.dto.AuthRequest;
import org.example.userservice.dto.RefreshTokenRequest;
import org.example.userservice.dto.TokenResponse;
import org.example.userservice.service.CustomUserDetailsService;
import org.example.userservice.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authentifier l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            // Charger les détails de l'utilisateur
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

                var roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // Générer les tokens
                String accessToken = jwtService.generateAccessToken(userDetails.getUsername(), roles);
            String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

            // Retourner la réponse
            TokenResponse response = new TokenResponse(
                    accessToken,
                    refreshToken,
                    15 * 60 // 15 minutes en secondes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            if (!jwtService.validateToken(refreshToken) ||
                    !jwtService.isRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid refresh token");
            }

            if (jwtService.isTokenExpired(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Refresh token expired");
            }

            String username = jwtService.getUsernameFromToken(refreshToken);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                var roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

                String newAccessToken = jwtService.generateAccessToken(username, roles);


            TokenResponse response = new TokenResponse(
                    newAccessToken
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token");
        }
    }
}