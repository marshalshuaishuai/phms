package com.mars.phms.controller;

import com.mars.phms.domain.PhArea;
import com.mars.phms.domain.PhMedicalHistory;
import com.mars.phms.domain.PhMember;
import com.mars.phms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.geom.Area;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mars
 * @create 2020-04-09 10:07
 */
@Controller
@RequestMapping("/medicalHistory")
public class MedicalHistoryController extends PhBaseController {
    @Autowired
    private MemberService memberService;
    @GetMapping
    public String toMemberMedicalHistoryManagerPage(
//            @RequestParam("memberId") long id,
            HttpServletRequest request,
            @ModelAttribute("medicalHistory") PhMedicalHistory medicalHistory,
            Model model){
        long id=Long.parseLong(request.getParameter("memberId"));
        PhMember member=memberService.findById(id);
        model.addAttribute("memberName",member.getName());
        model.addAttribute("memberId",id);
        List<PhMedicalHistory> medicalHistories=new ArrayList<>();
        model.addAttribute("medicalHistories",medicalHistories);
        List<PhArea> provinces=areaService.getProvinces();
        model.addAttribute("provinces",provinces);
        return "/medical_history/member_medical_history_manager";
    }
    @GetMapping("/save")
    public String toMemberMedicalHistoryAddOrUpdatePage(
            @ModelAttribute("medicalHistory") PhMedicalHistory medicalHistory,
            Model model){
        List<PhArea> provinces=areaService.getProvinces();
        model.addAttribute("provinces",provinces);
        if(medicalHistory.getArea()!=null){
            List<PhArea> cities=areaService.getCities(medicalHistory.getArea().getParentId());
            model.addAttribute("cities",cities);
        }

        return "/medical_history/member_medical_history_add_update";
    }
    @PostMapping("/save")
    public String doSaveMedicalHistory(
            @ModelAttribute("medicalHistory") @Validated PhMedicalHistory medicalHistory,
            BindingResult result){
        if(result.hasErrors()){
            return "/medical_history/member_medical_history_add_update";
        }
        return "redirect:/medicalHistory";
    }
    @GetMapping("/delete")
    public String doDeleteMedicalHistory(@RequestParam("id") long id){
        return "redirect:/medicalHistory";
    }
}
