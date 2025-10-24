package com.almoxarifado.almoxarifado_backend.dto;

public class AuthResponse {
    private String accessToken;
    private String role;

    public AuthResponse(String accessToken, String role) {
        this.accessToken = accessToken;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRole() {
        return role;
    }
}