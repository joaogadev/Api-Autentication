package com.api.apiautenticacao.Services;

import com.api.apiautenticacao.Model.RolesModel;
import com.api.apiautenticacao.repository.RolesRepository;

public class RoleServices {
    private final RolesRepository repo;

    public RoleServices(RolesRepository repo) {
        this.repo = repo;
    }

    public RolesModel getRole(String name) {
        return repo.findByName(name).orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }


}
