package com.api.apiautenticacao.repository;

import com.api.apiautenticacao.Model.TokensModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokensRepository extends JpaRepository<TokensModel, UUID> {
}
