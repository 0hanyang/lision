package com.lingxun.smartgunchecksystem.service.impl;

import com.lingxun.smartgunchecksystem.dto.UserLoginDto;
import com.lingxun.smartgunchecksystem.dto.UserLoginResponseDto;
import com.lingxun.smartgunchecksystem.entity.SysUser;
import com.lingxun.smartgunchecksystem.mapper.SysUserMapper;
import com.lingxun.smartgunchecksystem.security.jwt.JwtTokenProvider;
import com.lingxun.smartgunchecksystem.service.AuthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SysUserMapper sysUserMapper; // To get additional user details like unitId

    @Override
    public UserLoginResponseDto loginUser(UserLoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginDto.getUsername()));
        if (user == null) { // Should not happen if authentication succeeded
            throw new UsernameNotFoundException("User not found after authentication");
        }

        String token = jwtTokenProvider.generateToken(authentication, user);
        return new UserLoginResponseDto(token, user.getId(), user.getUsername(), user.getUnitId());
    }
}
