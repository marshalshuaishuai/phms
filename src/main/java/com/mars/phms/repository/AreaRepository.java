package com.mars.phms.repository;

import java.util.List;
import java.util.Optional;

import com.mars.phms.domain.PhArea;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<PhArea, Long> {
    Optional<PhArea> findById(Long id);

    List<PhArea> findByParentId(Long parentId);
}