package com.lingxun.smartgunchecksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Login Response DTO")
public class UserLoginResponseDto {
    @Schema(description = "JWT Token")
    private String token;
    @Schema(description = "User ID", example = "1")
    private Long userId;
    @Schema(description = "Username", example = "admin")
    private String username;
    @Schema(description = "Unit ID", example = "101")
    private Long unitId;
}
