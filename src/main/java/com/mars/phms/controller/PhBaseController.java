package com.mars.phms.controller;

import com.mars.phms.domain.PhArea;
import com.mars.phms.domain.PhUser;
import com.mars.phms.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author mars
 * @create 2020-04-07 15:43
 */
public class PhBaseController {
    @Autowired
    protected AreaService areaService;

    @GetMapping("/getCities")
    @ResponseBody
    public String getCities(@RequestParam Long parentId){
        StringBuilder sb=new StringBuilder();
        sb.append("<option value='-1'>请选择</option>");
        List<PhArea> areas=areaService.getCities(parentId);
        for (PhArea area : areas) {
            sb.append("<option value='" + area.getId() + "'>" + area.getName() + "</option>");
        }
        return sb.toString();
    }
}
