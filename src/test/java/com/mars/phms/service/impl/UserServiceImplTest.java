package com.mars.phms.service.impl;

import com.mars.phms.vo.UserRegisterInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mars
 * @create 2020-04-05 17:29
 */
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    private String username;
    private UserRegisterInfo basicInfo;

    @BeforeEach
    void setUp() {
        username="user";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getUserBasicInfo() {
        basicInfo=userService.getUserBasicInfo(username);
        //assertEquals(basicInfo.getArea(),null);
    }
}