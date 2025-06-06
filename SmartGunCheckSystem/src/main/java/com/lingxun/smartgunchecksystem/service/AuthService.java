package com.lingxun.smartgunchecksystem.service;

import com.lingxun.smartgunchecksystem.dto.UserLoginDto;
import com.lingxun.smartgunchecksystem.dto.UserLoginResponseDto;

public interface AuthService {
    UserLoginResponseDto loginUser(UserLoginDto loginDto);
}
