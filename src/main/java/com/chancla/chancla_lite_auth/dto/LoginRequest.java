package com.chancla.chancla_lite_auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}
