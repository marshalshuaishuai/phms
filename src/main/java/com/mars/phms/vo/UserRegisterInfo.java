package com.mars.phms.vo;

import com.mars.phms.domain.PhArea;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserRegisterInfo {
    private Long id;
    private String username;
    private String phone;
}