package com.api.apiautenticacao.Services;

import com.api.apiautenticacao.DTO.request.CreatedUserDTO;
import com.api.apiautenticacao.DTO.request.LoginRequestDTO;
import com.api.apiautenticacao.DTO.request.UpdateUserDTO;
import com.api.apiautenticacao.DTO.response.LoginResponseDTO;
import com.api.apiautenticacao.DTO.response.ResponseUserDTO;
import com.api.apiautenticacao.Model.PasswordResetTokenModel;
import com.api.apiautenticacao.Model.RolesModel;
import com.api.apiautenticacao.Model.UserModel;
import com.api.apiautenticacao.repository.RolesRepository;
import com.api.apiautenticacao.repository.UserRepository;
import com.api.apiautenticacao.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices {
    private UserRepository repo;
    private RolesRepository repoRoles;
    private PasswordEncoder passwordEncoder;
    private TokenServices tokenServices;
    private PasswordResetTokenRepository resetTokenRepo;

    public UserServices(UserRepository repo, RolesRepository repoRoles,
                        PasswordEncoder passwordEncoder, TokenServices tokenService,
                        PasswordResetTokenRepository resetTokenRepo) {
        this.repo = repo;
        this.repoRoles = repoRoles;
        this.passwordEncoder = passwordEncoder;
        this.tokenServices = tokenService;
        this.resetTokenRepo = resetTokenRepo;
    }

    //BLOCO DA AUTENTICAÇÃO
    @Transactional //Garante a integridade do banco de dados
    public ResponseUserDTO registerUser(CreatedUserDTO userDTO) {
        //Verifica se o email existe
        if (repo.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        //Caso o email não exista, ele salva o user com a role user
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

    public LoginResponseDTO loginUser(LoginRequestDTO loginDTO) {
        //Procurar usuario por email
        UserModel user = repo.findByEmail(loginDTO.email()).orElseThrow(() -> new RuntimeException("User not found with email: " + loginDTO.email()));

        //Verifica se a senha existe e se é igual a senha do banco de dados
        if (!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        //Se a senha bater, chamamos o TokenService para gerar os tokens
        // Aqui utiliza a geração do token JWT, que é o token de acesso, e o token refresh, que é o token de renovação
        String jwt = tokenServices.generateJwtToken(user);
        var refreshToken = tokenServices.generateToeknRefresh(user);

        return new LoginResponseDTO(jwt, refreshToken.getToken());
    }

    //BLOCO DO GERENCIAMENTO
    public void deleteUser(UUID id) {
        //Procura o usuario por ID, se não encontrar lança uma exceção
        UserModel user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        //Deleta o usuário encontrado
        repo.delete(user);
    }

    public ResponseUserDTO updateUpdate(UUID id, UpdateUserDTO userDTO) {
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

    //BLOCO DOS FIND´S
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

    @Transactional
    public void solicitarRecuperarSenha(String email) {
        //Buscar o usuário
        Optional<UserModel> userVerified = repo.findByEmail(email);
        //Apenas encerrar o método evita que possiveis invasores fiquem testando outros emails caso não encontrem um existente
        if (userVerified.isEmpty()) {
            return;
        }

        UserModel user = userVerified.get();

        //Limpa todos os tokens antigos do usuario
        resetTokenRepo.deleteByUser(user.getId());

        //Gera uma nova chave unica
        String newToken = UUID.randomUUID().toString();
        //Tempo para expirar o a chave unica
        Timestamp expiresTime = new Timestamp(System.currentTimeMillis() + 900000);
        //Nova entidade de token de reset de senha para o salvar no banco
        PasswordResetTokenModel newTokenModel = new PasswordResetTokenModel(newToken, user, expiresTime);
        //Salva o token no banco de dados
        resetTokenRepo.save(newTokenModel);

        //Lógica para envio do email
        System.out.println("🚨 [TESTE] Link de recuperação: http://localhost:8080/auth/redefinir-senha?token=" + newToken);
    }

    @Transactional
    public void redefinirSenha(String token, String newPassword) {

        //Valida se o token existe e se é válido, caso contrário lança uma exceção
        PasswordResetTokenModel resetToken = resetTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token de recuperação inválido ou inexistente."));

        //Exclui token que passaram da validade
        if (resetToken.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
            resetTokenRepo.delete(resetToken);
            throw new RuntimeException("Este link de recuperação expirou.");
        }

        UserModel user = resetToken.getUser(); //Busca o usuário associado
        user.setPassword(passwordEncoder.encode(newPassword));//criptografa a nova senha
        repo.save(user); //salva no banco

        resetTokenRepo.delete(resetToken); //Limpa o token do banco, para evitar reutilização
    }
}
