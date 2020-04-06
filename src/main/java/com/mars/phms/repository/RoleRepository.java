package com.mars.phms.repository;

import com.mars.phms.domain.PhRole;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<PhRole,Long>{
    PhRole findByAuthority(String authority);
}