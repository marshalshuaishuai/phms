package com.mars.phms.repository;

import com.mars.phms.domain.PhMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author mars
 * @create 2020-04-07 16:15
 */
public interface MemberRepository extends JpaRepository<PhMember,Long> {
    List<PhMember> findByUserId(long uId);
}
