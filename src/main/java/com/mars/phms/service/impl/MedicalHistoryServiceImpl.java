package com.mars.phms.service.impl;

import com.mars.phms.domain.PhMedicalHistory;
import com.mars.phms.repository.MedicalHistoryRepository;
import com.mars.phms.service.MedicalHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @author mars
 * @create 2020-04-10 15:36
 */
@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    @Autowired
    MedicalHistoryRepository medicalHistoryRepository;

    @Override
    public PhMedicalHistory findById(long id) {
        return medicalHistoryRepository.findById(id).orElse(null);
    }

    @Override
    public Page<PhMedicalHistory> findAllPaged(Example<PhMedicalHistory> medicalHistoryExample, int pageNum, int pageSize) {
        Sort sort=Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(pageNum,pageSize,sort);
        Page<PhMedicalHistory> medicalHistories=medicalHistoryRepository.findAll(medicalHistoryExample,pageable);
        return medicalHistories;
    }

    @Override
    public PhMedicalHistory save(PhMedicalHistory medicalHistory) {
        return medicalHistoryRepository.save(medicalHistory);
    }

    @Override
    public void deleteById(long id) {
        medicalHistoryRepository.deleteById(id);
    }
}
