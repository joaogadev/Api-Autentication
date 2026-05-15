package com.api.apiautenticacao.repository;

import com.api.apiautenticacao.Model.PasswordResetTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenModel, UUID> {
    Optional<PasswordResetTokenModel> findByToken(String token);

    void deleteByUser(UUID user);
}
