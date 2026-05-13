package com.api.apiautenticacao.Services;

import com.api.apiautenticacao.DTO.request.CreatedUserDTO;
import com.api.apiautenticacao.DTO.request.UpdateUserDTO;
import com.api.apiautenticacao.DTO.response.ResponseUserDTO;
import com.api.apiautenticacao.Model.RolesModel;
import com.api.apiautenticacao.Model.UserModel;
import com.api.apiautenticacao.repository.RolesRepository;
import com.api.apiautenticacao.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

public class UserServices {
    private UserRepository repo;
    private RolesRepository repoRoles;
    private PasswordEncoder passwordEncoder;


    public ResponseUserDTO save(CreatedUserDTO userDTO) {
        //Verifica se o email existe
        if (repo.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        //Caso o email já exista, ele salva o user com a role user
        RolesModel role = repoRoles
                .findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role 'USER' não encontrada"));

        //Cadastra o usuário com as informações dele
        UserModel user = new UserModel(
                role,
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword())

        );

        //Salva o usuário no banco de dados
        UserModel savedUser = repo.save(user);

        //Retorna o usuario cadastrado
        return new ResponseUserDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().getName()
        );
    }

    public void delete(UUID id) {
        //Procura o usuario por ID, se não encontrar lança uma exceção
        UserModel user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        //Deleta o usuário encontrado
        repo.delete(user);
    }

    public ResponseUserDTO update(UUID id, UpdateUserDTO userDTO) {
        //procura o usuario por ID
        UserModel user = repo
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        //Atualiiza a variuavel user com email e senha novos
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //Atualiza o usuario no banco de dados
        UserModel userUpdate = repo.save(user);

        //Retorna os dados
        return new ResponseUserDTO(
                userUpdate.getId(),
                userUpdate.getEmail(),
                userUpdate.isActive(),
                userUpdate.isVerified()
        );

    }
    public ResponseUserDTO findByEmail(String email) {
        //Procura o usuario por email
        return repo.findByEmail(email)
                //Trasforma manualmente o UserModel do repo em um RespondeUserDTO
                .map(userDTO -> new ResponseUserDTO(
                        userDTO.getId(),
                        userDTO.getEmail(),
                        userDTO.isActive(),
                        userDTO.isVerified()
                        //Lanca exceção caso não ache
                )).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public ResponseUserDTO findByID(UUID id) {
        //Proucura o usuario por ID
        return repo.findById(id)
                //Trasforma manualmente o UserModel do repo em um RespondeUserDTO
                .map(user -> new ResponseUserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getRole(),
                        user.isVerified(),
                        user.isActive()
                )).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public List<ResponseUserDTO> findAllUsers() {
        //Lista todos os usuários
        return repo.findAll()
                //Trasforma manualmente o UserModel do repo em um RespondeUserDTO
                .stream().map(user -> new ResponseUserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getRole(),
                        user.isVerified(),
                        user.isActive()
                )).toList();
    }
}
