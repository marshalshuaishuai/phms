package com.mars.phms.vo;

import com.mars.phms.domain.PhArea;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserBasicInfo {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String sex;
    private String detailAddress;
    private Long areaId;
    private Long areaParentId;
}