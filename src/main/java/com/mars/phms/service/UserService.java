package com.mars.phms.service;

import com.mars.phms.domain.PhUser;
import com.mars.phms.vo.UserBasicInfo;

import org.springframework.security.core.userdetails.UserDetailsService;
public interface UserService extends UserDetailsService{
    PhUser saveUser(PhUser user);
    PhUser findByName(String username);
    void updateUserBasicInfo(UserBasicInfo userinfo);
    UserBasicInfo getUserBasicInfo(String username);
}