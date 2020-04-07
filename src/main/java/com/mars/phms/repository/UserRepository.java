package com.mars.phms.repository;

import javax.transaction.Transactional;

import com.mars.phms.domain.PhUser;
import com.mars.phms.vo.UserBasicInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<PhUser,Long>{
    PhUser findByUsername(String username);
    PhUser findByEmail(String email);

    @Query("select new com.mars.phms.vo.UserBasicInfo(u.id,u.username,u.realName,u.phone,u.sex,u.detailAddress,a.id,a.parentId)" +
            " from PhUser u left join PhArea a on u.area.id=a.id where u.username=:name")
    UserBasicInfo getUserBasicInfo(@Param("name") String username);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update tb_user set " +
            "real_name=:#{#user.realName}," +
            "sex=:#{#user.sex}," +
            "phone=:#{#user.phone}," +
            "area_id=:#{#user.areaId}," +
            "detail_address=:#{#user.detailAddress} " +
            "where id=:#{#user.id}")
    void updateUserBasicInfo(@Param("user") UserBasicInfo userBasicInfo);


    @Modifying
    @Transactional
    @Query(nativeQuery = true,
    value = "update tb_user set password=:password where username=:username")
    void changeUserPassword(@Param("username") String username, @Param("password") String encodedPassword);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update tb_user set email=:email where username=:username")
    void changeEmail(@Param("username") String username,@Param("email") String email);
}