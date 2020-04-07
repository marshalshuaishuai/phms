/*
 * @Author: mars
 * @Date: 2020-03-28 14:53:58
 * @Last Modified by: mars
 * @Last Modified time: 2020-04-04 23:12:03
 */
package com.mars.phms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mars.phms.domain.PhRole;
import com.mars.phms.domain.PhUser;
import com.mars.phms.repository.RoleRepository;
import com.mars.phms.repository.UserRepository;
import com.mars.phms.service.UserService;
import com.mars.phms.vo.UserBasicInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PhUser saveUser(PhUser user) {
        // 如果用户名和密码都不为空，写入数据库
        if (!StringUtils.isEmpty(user.getUsername()) && !StringUtils.isEmpty(user.getPassword())) {
            // 密码加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            PhRole role = roleRepository.findByAuthority("ROLE_USER");
            List<PhRole> roles = new ArrayList<>();
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);
        } else {
            user = null;
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        PhUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    @Override
    public PhUser findByName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUserBasicInfo(UserBasicInfo basicInfo) {
        userRepository.updateUserBasicInfo(basicInfo);
    }

    @Override
    public UserBasicInfo getUserBasicInfo(String username) {
        return userRepository.getUserBasicInfo(username);
    }

    @Override
    public boolean changeUserPassword(String username,String oldPassword,String newPassword) {
        boolean result=false;
        PhUser user=userRepository.findByUsername(username);
        if(user!=null&&passwordEncoder.matches(oldPassword,user.getPassword())){
            userRepository.changeUserPassword(username,passwordEncoder.encode(newPassword));
            result=true;
        }
        return result;
    }

    @Override
    public boolean isEmailUsed(String email) {
        if(userRepository.findByEmail(email)!=null)
            return true;
        else
            return false;
    }

    @Override
    public void changeEmail(String username,String email) {
        userRepository.changeEmail(username,email);
    }


}