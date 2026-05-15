package com.api.apiautenticacao.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

public class PasswordResetTokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    //Token que será enviado por email para o usuário, para que ele possa resetar a senha
    private String token;

    //Relacionamento OneToOne com a entidade UserModel, onde cada token de reset de senha está associado a um único usuário
    @OneToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserModel user;

    @Column(nullable = false)
    private Timestamp expiresAt;

    public PasswordResetTokenModel() {}

    public PasswordResetTokenModel(String token, UserModel user, Timestamp expiresAt) {
        this.token = token;
        this.user = user;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
}
