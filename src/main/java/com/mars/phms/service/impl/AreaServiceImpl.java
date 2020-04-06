/*
 * @Author: mars
 * @Date: 2020-04-04 09:04:40
 * @Last Modified by: mars
 * @Last Modified time: 2020-04-04 09:12:54
 */
package com.mars.phms.service.impl;

import java.util.List;

import com.mars.phms.domain.PhArea;
import com.mars.phms.repository.AreaRepository;
import com.mars.phms.service.AreaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository areaRepository;
    @Override
    public List<PhArea> getProvinces() {
        return areaRepository.findByParentId(0L);
    }

    @Override
    public List<PhArea> getCities(Long parentId) {
        return areaRepository.findByParentId(parentId);
    }

}