package com.mars.phms.repository;

import javax.transaction.Transactional;

import com.mars.phms.domain.PhUser;
import com.mars.phms.vo.UserRegisterInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<PhUser,Long>{
    PhUser findByUsername(String username);
    PhUser findByEmail(String email);

    @Query("select new com.mars.phms.vo.UserRegisterInfo(u.id,u.username,u.phone)" +
            " from PhUser u where u.username=:name")
    UserRegisterInfo getUserBasicInfo(@Param("name") String username);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update tb_user set " +
            "phone=:#{#user.phone} " +
            "where id=:#{#user.id}")
    void updateUserBasicInfo(@Param("user") UserRegisterInfo userRegisterInfo);


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