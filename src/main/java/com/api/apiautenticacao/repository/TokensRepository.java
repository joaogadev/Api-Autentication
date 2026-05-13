package com.api.apiautenticacao.repository;

import com.api.apiautenticacao.Model.TokensModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokensRepository extends JpaRepository<TokensModel, Long> {
}
