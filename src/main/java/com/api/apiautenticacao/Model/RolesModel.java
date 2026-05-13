package com.api.apiautenticacao.Model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.security.*;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class RolesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String description;

    @Column(unique = true, nullable = false)
    private Timestamp created_at;

    public RolesModel() {

    }
    public RolesModel(String name, String description, Timestamp created_at) {
        this.name = name;
        this.description = description;
        this.created_at = created_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
