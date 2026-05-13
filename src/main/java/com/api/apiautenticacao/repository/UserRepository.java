package com.api.apiautenticacao.repository;

import com.api.apiautenticacao.DTO.response.ResponseUserDTO;
import com.api.apiautenticacao.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    boolean existsByEmail(String email);
    boolean existsById(UUID id);

    Optional<ResponseUserDTO> findByEmail(String email);
}
