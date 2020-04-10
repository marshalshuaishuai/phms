package com.mars.phms.repository;

import com.mars.phms.domain.PhUser;
import com.mars.phms.vo.UserRegisterInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mars
 * @create 2020-04-05 19:03
 */
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByUsername() {
//        PhUser user=userRepository.findByUsername("user");
//        assertNotNull(user);
    }

    @Test
    void getUserBasicInfo() {
//        UserRegisterInfo basicInfo=userRepository.getUserBasicInfo("user");
//        assertNotNull(basicInfo);
    }

    @Test
    void updateUserBasicInfo() {
    }
}