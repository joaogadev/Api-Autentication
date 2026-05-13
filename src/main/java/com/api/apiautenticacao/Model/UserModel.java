package com.api.apiautenticacao.Model;

import java.sql.Timestamp;
import java.security.*;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RolesModel role_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active")
    private boolean is_active;

    @Column(name = "is_verified")
    private boolean is_verified;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "updated_at")
    private Timestamp updated_at;

    public UserModel() {

    }

    public UserModel(RolesModel role_id, String email, String password, boolean is_active, boolean is_verified, Timestamp created_at, Timestamp updated_at) {
        this.role_id = role_id;
        this.email = email;
        this.password = password;
        this.is_active = false;
        this.is_verified = false;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RolesModel getRole_id() {
        return role_id;
    }

    public void setRole_id(RolesModel role_id) {
        this.role_id = role_id;
    }
}
