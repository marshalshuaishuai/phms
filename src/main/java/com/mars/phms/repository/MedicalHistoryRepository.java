package com.mars.phms.repository;

import com.mars.phms.domain.PhMedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mars
 * @create 2020-04-10 15:53
 */
public interface MedicalHistoryRepository extends JpaRepository<PhMedicalHistory,Long> {
}
