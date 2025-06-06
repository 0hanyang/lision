package com.lingxun.smartgunchecksystem.controller;

import com.lingxun.smartgunchecksystem.common.response.UnifiedResponse;
import com.lingxun.smartgunchecksystem.dto.UserLoginDto;
import com.lingxun.smartgunchecksystem.dto.UserLoginResponseDto;
import com.lingxun.smartgunchecksystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticates a user and returns a JWT token.")
    public ResponseEntity<UnifiedResponse<UserLoginResponseDto>> login(@Valid @RequestBody UserLoginDto loginDto) {
        UserLoginResponseDto loginResponse = authService.loginUser(loginDto);
        return ResponseEntity.ok(UnifiedResponse.success("Login successful", loginResponse));
    }
}
