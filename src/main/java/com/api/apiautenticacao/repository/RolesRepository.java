package com.api.apiautenticacao.repository;

import com.api.apiautenticacao.Model.RolesModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RolesRepository extends JpaRepository<RolesModel, UUID> {
    Optional <RolesModel> findByName(String name);
}
