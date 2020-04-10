package com.mars.phms.service;

import com.mars.phms.domain.PhMember;
import com.mars.phms.vo.MemberRecentDiseaseVo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

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

    /**
     * 根据条件查找对应成员
     * @param memberExample
     * @return
     */
    List<PhMember> findAll(Example<PhMember> memberExample);

    /**
     * 根据ID删除成员
     * @param id
     */
    void deleteMember(long id);

    /**
     * 根据条件分页查找对应成员
     * @param memberExample 查找条件
     * @param pageNum 当前页
     * @param pageSize 每页记录数
     * @return 查找到的成员列表
     */
    Page<PhMember> findAllPaged(Example<PhMember> memberExample, int pageNum, int pageSize);

    /**
     * 根据用户名
     * @param username
     * @return
     */
    List<MemberRecentDiseaseVo> findRecentDiseaseForMember(String username);
}
