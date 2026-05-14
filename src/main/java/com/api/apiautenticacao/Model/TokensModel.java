package com.api.apiautenticacao.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class TokensModel {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
    private String token;
    private Timestamp expires_at;
    private boolean is_revoked;
    private Timestamp created_at;

    public TokensModel() {

    }

    public TokensModel(UserModel user, String token, Timestamp expires_at, boolean is_revoked, Timestamp created_at) {
        this.user = user;
        this.token = token;
        this.expires_at = expires_at;
        this.is_revoked = false;
        this.created_at = created_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Timestamp expires_at) {
        this.expires_at = expires_at;
    }

    public boolean isIs_revoked() {
        return is_revoked;
    }

    public void setIs_revoked(boolean is_revoked) {
        this.is_revoked = is_revoked;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
