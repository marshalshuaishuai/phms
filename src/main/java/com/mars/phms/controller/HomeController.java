package com.mars.phms.controller;

import java.util.List;

import com.mars.phms.domain.PhArea;
import com.mars.phms.service.AreaService;
import com.mars.phms.service.UserService;
import com.mars.phms.vo.UserBasicInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class HomeController {
    @Autowired
    private AreaService areaService;

    @GetMapping
    public String toHomePage(Model model) {
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", loggedInUserName);
        return "home";
    }


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