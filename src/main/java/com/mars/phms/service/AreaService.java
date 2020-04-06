package com.mars.phms.service;

import java.util.List;

import com.mars.phms.domain.PhArea;

public interface AreaService {
    List<PhArea> getProvinces();

    List<PhArea> getCities(Long parentId);
}