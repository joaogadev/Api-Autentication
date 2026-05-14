package com.api.apiautenticacao.Model;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RolesModel role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updateAt;

    public UserModel() {

    }

    public UserModel(RolesModel role, String email, String password) {
        this.role = role;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.isVerified = false;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updateAt = new Timestamp(System.currentTimeMillis());
    }

    public UUID getId() {
        return id;
    }

    public RolesModel getRole() {
        return role;
    }

    public void setRole(RolesModel role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email no formato inválido.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("A senha deve conter pelo menos 8 caracteres.");
        } else {
            this.password = password;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
