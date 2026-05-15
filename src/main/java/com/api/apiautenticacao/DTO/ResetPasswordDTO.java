package com.api.apiautenticacao.DTO;

public record ResetPasswordDTO(String token, String newPassword) {
}
