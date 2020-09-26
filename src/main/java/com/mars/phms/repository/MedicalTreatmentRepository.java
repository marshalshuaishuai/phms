package com.mars.phms.repository;

import com.mars.phms.domain.PhMedicalTreatment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mars
 * @create 2020-04-10 18:02
 */
public interface MedicalTreatmentRepository extends JpaRepository<PhMedicalTreatment,Long> {
}
