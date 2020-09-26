package com.mars.phms.service;

import com.mars.phms.domain.PhUser;
import com.mars.phms.vo.UserRegisterInfo;

import org.springframework.security.core.userdetails.UserDetailsService;
public interface UserService extends UserDetailsService{
    PhUser saveUser(PhUser user);
    PhUser findByName(String username);
    void updateUserBasicInfo(UserRegisterInfo userinfo);
    UserRegisterInfo getUserBasicInfo(String username);
    boolean changeUserPassword(String username,String oldPassword,String newPassword);

    /**
     * 判断邮箱是否已被占用
     * @param email 邮箱地址
     * @return
     */
    boolean isEmailUsed(String email);

    /**
     * 变更邮箱
     * @param email
     * @return
     */
    void changeEmail(String username,String email);
}