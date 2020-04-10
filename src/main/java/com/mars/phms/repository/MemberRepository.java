package com.mars.phms.repository;

import com.mars.phms.domain.PhMember;
import com.mars.phms.vo.MemberRecentDiseaseVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author mars
 * @create 2020-04-07 16:15
 */
public interface MemberRepository extends JpaRepository<PhMember,Long> {
    List<PhMember> findByUserId(long uId);
//    @Query("select new com.mars.phms.vo.MemberRecentDiseaseVo(m.id,m.name,m.birthday,m.name,m.name) from " +
//            "PhUser u,PhMember m where u.id=m.user.id and u.username=:name")
@Query("select new com.mars.phms.vo.MemberRecentDiseaseVo(m.id,m.name,m.birthday,m.name,m.name) from " +
        "PhMember m where m.user.username=:name")
    List<MemberRecentDiseaseVo> findRecentDiseaseForMember(@Param("name") String username);
}
