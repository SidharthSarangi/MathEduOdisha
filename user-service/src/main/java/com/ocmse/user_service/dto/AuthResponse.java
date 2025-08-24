package com.ocmse.user_service.dto;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private String role ;
    public AuthResponse(String token, String role){
        this.token = token ;
        this.role = role ;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
