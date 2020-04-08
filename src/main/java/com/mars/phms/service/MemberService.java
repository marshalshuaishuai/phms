package com.mars.phms.service;

import com.mars.phms.domain.PhMember;

import java.util.List;

/**
 * @author mars
 * @create 2020-04-07 16:18
 */
public interface MemberService {
    /**
     * 通过用户ID查找所属成员
     * @param uId
     * @return
     */
    List<PhMember> findByUserId(long uId);

    /**
     * 根据ID查找对应的成员
     * @param id
     * @return
     */
    PhMember findById(long id);

    /**
     * 将member持久化到数据库
     * @param member
     * @return
     */
    PhMember saveMember(PhMember member);
}
