package com.api.apiautenticacao.Services;

import com.api.apiautenticacao.Model.UserModel;
import com.api.apiautenticacao.repository.UserRepository;

public class UserServices {
    private UserRepository repo;

    public UserModel save(UserModel user) {
        if (repo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        } else {
            return repo.save(user);
        }
    }
    public void delete(UserModel user) {
        if (!repo.existsById(user.getId())) {
            throw new RuntimeException("User not found with id: " + user.getId());
        } else {
            repo.delete(user);
        }
    }


}
