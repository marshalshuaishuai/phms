package com.mars.phms.service;

import com.mars.phms.domain.PhMedicalTreatment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

/**
 * @author mars
 * @create 2020-04-10 17:58
 */
public interface MedicalTreatmentService {
    PhMedicalTreatment findById(long id);
    Page<PhMedicalTreatment> findAllPaged(Example<PhMedicalTreatment> medicalTreatmentExample,int pageNum,int pageSize);
    PhMedicalTreatment save(PhMedicalTreatment medicalTreatment);
    void deleteById(long id);
}
