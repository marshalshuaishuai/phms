package com.mars.phms.service;

import com.mars.phms.domain.PhMedicalHistory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

/**
 * @author mars
 * @create 2020-04-10 15:33
 */
public interface MedicalHistoryService {
    PhMedicalHistory findById(long id);
    Page<PhMedicalHistory> findAllPaged(Example<PhMedicalHistory> medicalHistoryExample,int pageNum,int pageSize);
    PhMedicalHistory save(PhMedicalHistory medicalHistory);
    void deleteById(long id);
}
