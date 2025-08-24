package com.ocmse.user_service.controller;

import com.ocmse.user_service.dto.AuthRequest;
import com.ocmse.user_service.dto.AuthResponse;
import com.ocmse.user_service.dto.GoogleLoginRequest;
import com.ocmse.user_service.dto.RegisterRequest;
import com.ocmse.user_service.model.User;
import com.ocmse.user_service.security.JwtUtils;
import com.ocmse.user_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils ;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        AuthResponse response = authService.loginWithGoogle(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, String>> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);

        if (!authService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwtUtils.getUserIdFromToken(token);
        String email = jwtUtils.getEmailFromToken(token);
        String role = jwtUtils.getRoleFromToken(token);

        Map<String, String> response = new HashMap<>();
        response.put("userId", userId);
        response.put("email", email);
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/role/{email}")
    public ResponseEntity<String> getRole(@PathVariable String email) {
        String role = authService.getUserRole(email);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/update-role/{targetEmail}/{role}")
    public ResponseEntity<Void> updateRole(@PathVariable String targetEmail,
                                           @PathVariable String role) {
        // ✅ Get the authenticated user's email from Spring Security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterEmail = auth.getName();
        authService.updateUserRole(requesterEmail, targetEmail, User.Role.valueOf(role.toUpperCase()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists/id/{userId}")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID userId) {
        boolean exists = authService.userExistsById(userId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = authService.userExistsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/is-author/{email}")
    public ResponseEntity<Boolean> isAuthor(@PathVariable String email) {
        boolean isAuthor = authService.isUserAuthor(email);
        return ResponseEntity.ok(isAuthor);
    }

    @GetMapping("/is-admin/{email}")
    public ResponseEntity<Boolean> isAdmin(@PathVariable String email){
        boolean isAdmin = authService.isUserAdmin(email) ;
        return ResponseEntity.ok(isAdmin) ;
    }
}
