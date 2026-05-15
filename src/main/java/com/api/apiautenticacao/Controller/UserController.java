package com.api.apiautenticacao.Controller;

import com.api.apiautenticacao.DTO.ForgotPasswordDTO;
import com.api.apiautenticacao.DTO.ResetPasswordDTO;
import com.api.apiautenticacao.DTO.request.CreatedUserDTO;
import com.api.apiautenticacao.DTO.request.LoginRequestDTO;
import com.api.apiautenticacao.DTO.response.LoginResponseDTO;
import com.api.apiautenticacao.DTO.response.ResponseUserDTO;
import com.api.apiautenticacao.Services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDTO> register(@RequestBody CreatedUserDTO userDTO) {
        //Cadastrando o user com, porem filtrado com os atributos do ResponseUserDTO, para não retornar a senha
        ResponseUserDTO responseUserDTO = userServices.registerUser(userDTO);
        return ResponseEntity.ok(responseUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginDTO) {
        LoginResponseDTO response = userServices.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> reserPassword(@RequestBody ResetPasswordDTO rp) {
        userServices.redefinirSenha(rp.token(), rp.newPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordDTO fp) {
        // Lógica para recuperar a senha do usuário com base no email
        userServices.solicitarRecuperarSenha(fp.email());
        return ResponseEntity.ok().build();

    }
}
