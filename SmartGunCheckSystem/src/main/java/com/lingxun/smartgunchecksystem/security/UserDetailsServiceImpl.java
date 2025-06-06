package com.lingxun.smartgunchecksystem.security;

import com.lingxun.smartgunchecksystem.entity.SysUser;
import com.lingxun.smartgunchecksystem.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if (!user.getEnabled()) {
             throw new UsernameNotFoundException("User disabled: " + username);
        }

        Set<String> roleKeys = sysUserMapper.findRoleKeysByUserId(user.getId());
        Set<GrantedAuthority> authorities = roleKeys.stream()
                .map(roleKey -> new SimpleGrantedAuthority("ROLE_" + roleKey.toUpperCase())) // Prefix with ROLE_ for Spring Security
                .collect(Collectors.toSet());

        // Add a default "USER" role if no specific roles are found, or handle as per requirements
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities);
    }
}
