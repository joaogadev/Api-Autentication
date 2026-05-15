package com.api.apiautenticacao.Services;

import com.api.apiautenticacao.DTO.response.ResponseUserDTO;
import com.api.apiautenticacao.Model.PasswordResetTokenModel;
import com.api.apiautenticacao.Model.UserModel;
import com.api.apiautenticacao.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PasswordResetTokenServices {
    private PasswordResetTokenRepository repo;
    public PasswordResetTokenServices(PasswordResetTokenRepository repo) {this.repo = repo;}

    public ResponseUserDTO validatePasswordResetToken(String token) {

        // 3. Busca o token no banco
        PasswordResetTokenModel resetToken = repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token de recuperação inválido ou inexistente."));

        // 4. Verificação de Segurança: O token expirou?
        if (resetToken.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
            repo.delete(resetToken); // Limpa o banco se já expirou
            throw new RuntimeException("Este link de recuperação expirou.");
        }

        // 5. Se estiver tudo OK, pegamos o usuário dono do token
        UserModel user = resetToken.getUser();

        // 6. Retornamos o DTO (convertendo o Model para DTO)
        return new ResponseUserDTO(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.isVerified(),
                user.isActive()
        );
    }
}
