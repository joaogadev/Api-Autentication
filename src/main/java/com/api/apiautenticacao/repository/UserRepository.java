package com.api.apiautenticacao.repository;

import com.api.apiautenticacao.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    boolean existsByEmail(String email);
    UserModel existsById(long id);
}
