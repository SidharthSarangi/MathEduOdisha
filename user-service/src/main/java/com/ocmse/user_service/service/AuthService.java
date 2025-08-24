package com.ocmse.user_service.service;

import com.ocmse.user_service.dto.AuthRequest;
import com.ocmse.user_service.dto.AuthResponse;
import com.ocmse.user_service.dto.GoogleLoginRequest;
import com.ocmse.user_service.dto.RegisterRequest;
import com.ocmse.user_service.model.User;
import com.ocmse.user_service.repository.UserRepository;
import com.ocmse.user_service.security.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ✅ Register new user
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setName(request.getName());
        user.setRole(User.Role.USER); // default role

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        return new AuthResponse(token,"USER");
    }

    // ✅ Email/Password login
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        return new AuthResponse(token,user.getRole().name());
    }

    // ✅ Google login (backend validation, simplified)
    @Transactional
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) {
        // This assumes frontend sends a valid Google ID token
        // In production, validate with Google's tokeninfo endpoint or library
        String email = request.getEmail();
        String providerId = request.getProviderId();

        Optional<User> existing = userRepository.findByEmail(email);
        User user;

        if (existing.isPresent()) {
            user = existing.get();
        } else {
            user = new User();
            user.setId(UUID.randomUUID());
            user.setEmail(email);
            user.setProvider("google");
            user.setProviderId(providerId);
            user.setRole(User.Role.USER);
            userRepository.save(user);
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name(), user.getId().toString());
        return new AuthResponse(token,user.getRole().name());
    }


    // ✅ Get user role
    public String getUserRole(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole().name();
    }

    // ✅ Admin-only: update user role
    @Transactional
    public void updateUserRole(String requesterEmail, String targetEmail, User.Role newRole) {

        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new RuntimeException("Requester Admin user not found"));

        if (requester.getRole() != User.Role.ADMIN) {
            throw new AccessDeniedException("Only admins can update user roles.");
        }

        User target = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        target.setRole(newRole);
        userRepository.save(target);
    }

    // ✅ Check if user exists by ID
    public boolean userExistsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    // ✅ Check if user exists by email
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // ✅ Check if user is AUTHOR
    public boolean isUserAuthor(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRole() == User.Role.AUTHOR;
    }

    // check if user is ADMIN
    public boolean isUserAdmin(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found")) ;
        return user.getRole() == User.Role.ADMIN;
    }

    public boolean isTokenValid(String token) {
        try {
            jwtUtils.validateToken(token) ;
            return true ;
        }catch (JwtException e) {
            return false ;
        }
    }

}