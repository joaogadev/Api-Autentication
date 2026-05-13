package com.api.apiautenticacao.DTO.response;

import com.api.apiautenticacao.Model.RolesModel;

import java.util.UUID;

public class ResponseUserDTO {
    private UUID id;
    private String email;
    private boolean active;
    private boolean verified;
    
    public ResponseUserDTO() {

    }

    public ResponseUserDTO(UUID id, String email, boolean active, boolean verified) {
        this.id = id;
        this.email = email;
        this.active = active;
        this.verified = verified;
    }

    public ResponseUserDTO(UUID id, String email, String name) {
    }

    public ResponseUserDTO(UUID id, String email, RolesModel role, boolean verified, boolean active) {
    }

    public boolean isVerified() {
        return verified;
    }
    public boolean isActive() {
        return active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email no formato inválido.");
        }
    }
}
