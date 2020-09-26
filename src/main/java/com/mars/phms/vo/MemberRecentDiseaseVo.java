package com.mars.phms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author mars
 * @create 2020-04-09 7:03
 */
@Data
@AllArgsConstructor
public class MemberRecentDiseaseVo {
    private long memberId;
    private String memberName;
    private Date recentIllDate;
    private String recentIllArea;
    private String disease;
}
