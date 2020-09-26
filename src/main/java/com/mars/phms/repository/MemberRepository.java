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

    @Query("select new com.mars.phms.vo.MemberRecentDiseaseVo(m.id,m.name,h.illDate,a.name,h.disease) from " +
        "PhMember m left join m.medicalHistories h left join h.area a where m.user.username=:name group by m.id")
    List<MemberRecentDiseaseVo> findRecentDiseaseForMember(@Param("name") String username);
}
