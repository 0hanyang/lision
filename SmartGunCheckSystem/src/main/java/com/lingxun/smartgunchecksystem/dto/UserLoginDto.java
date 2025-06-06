package com.lingxun.smartgunchecksystem.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User Login Request DTO")
public class UserLoginDto {
    @NotBlank(message = "Username cannot be blank")
    @Schema(description = "Username", example = "admin")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "Password", example = "password123")
    private String password;
}
