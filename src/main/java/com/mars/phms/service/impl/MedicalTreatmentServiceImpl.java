package com.mars.phms.service.impl;

import com.mars.phms.domain.PhMedicalTreatment;
import com.mars.phms.repository.MedicalHistoryRepository;
import com.mars.phms.repository.MedicalTreatmentRepository;
import com.mars.phms.service.MedicalTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @author mars
 * @create 2020-04-10 17:58
 */
@Service
public class MedicalTreatmentServiceImpl implements MedicalTreatmentService {
    @Autowired
    private MedicalTreatmentRepository medicalTreatmentRepository;

    @Override
    public PhMedicalTreatment findById(long id) {
        return medicalTreatmentRepository.findById(id).orElse(null);
    }

    @Override
    public Page<PhMedicalTreatment> findAllPaged(Example<PhMedicalTreatment> medicalTreatmentExample, int pageNum, int pageSize) {
        Sort sort=Sort.by(Sort.Direction.ASC,"medicalDate");
        Pageable pageable= PageRequest.of(pageNum,pageSize,sort);
        return medicalTreatmentRepository.findAll(medicalTreatmentExample,pageable);
    }

    @Override
    public PhMedicalTreatment save(PhMedicalTreatment medicalTreatment) {
        return medicalTreatmentRepository.save(medicalTreatment);
    }

    @Override
    public void deleteById(long id) {
        medicalTreatmentRepository.deleteById(id);
    }
}
