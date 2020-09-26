package com.mars.phms.controller;

import com.mars.phms.constant.PhParam;
import com.mars.phms.domain.PhMedicalHistory;
import com.mars.phms.domain.PhMedicalTreatment;
import com.mars.phms.service.MedicalHistoryService;
import com.mars.phms.service.MedicalTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mars
 * @create 2020-04-10 17:57
 */
@Controller
@RequestMapping("/medicalTreatment")
public class MedicalTreatmentController {
    @Autowired
    private MedicalHistoryService historyService;
    @Autowired
    private MedicalTreatmentService treatmentService;

    @GetMapping
    public String toMedicalTreatmentManagerPage(
            @ModelAttribute("medicalTreatment") PhMedicalTreatment medicalTreatment,
            @RequestParam(name = "pageNum",defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize",defaultValue = PhParam.PAGE_DEFAULT_SIZE) int pageSize,
            @RequestParam("memberId") long memberId,
            HttpServletRequest request,
            Model model){
        //得到正在管理的病史，将其返回页面用于显示正在管理哪个病史，将ID返回页面用于下次查询时知道在管理哪个病史
        long historyId=Long.parseLong(request.getParameter("historyId"));
        PhMedicalHistory history=historyService.findById(historyId);
        model.addAttribute("history",history);
        model.addAttribute("historyId",historyId);
        //如果用户输入的每页显示数据条数大于最大值或小于最小值，设置为默认值
        if(pageSize<PhParam.PAGE_MIN_SIZE||pageSize>PhParam.PAGE_MAX_SIZE)
            pageSize=Integer.valueOf(PhParam.PAGE_DEFAULT_SIZE);
        //得到查询条件
        medicalTreatment.setMedicalHistory(history);
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("id");
        Example<PhMedicalTreatment> medicalTreatmentExample=Example.of(medicalTreatment,matcher);
        //查询并将结果返回页面
        Page<PhMedicalTreatment> medicalTreatments=treatmentService.findAllPaged(medicalTreatmentExample,pageNum,pageSize);
        model.addAttribute("medicalTreatments",medicalTreatments);

        //将每页查询记录条数返回页面
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("memberId",memberId);
        return "medical_history/member_medical_treatment_manager";
    }

    @GetMapping("/add")
    public String toMedicalTreatmentAddPage(
            @ModelAttribute("medicalTreatment") PhMedicalTreatment medicalTreatment,
            @RequestParam("historyId") long historyId,
            @RequestParam("memberId") long memberId,
            Model model){
        model.addAttribute("memberId",memberId);
        model.addAttribute("historyId",historyId);
        return "medical_history/member_medical_treatment_add_update";
    }

    @GetMapping("/update")
    public String toMedicalTreatmentUpdatePage(
            @RequestParam("id") long id,
            @RequestParam("historyId") long historyId,
            @RequestParam("memberId") long memberId,
            Model model){
        PhMedicalTreatment medicalTreatment=treatmentService.findById(id);
        model.addAttribute("medicalTreatment",medicalTreatment);
        model.addAttribute("memberId",memberId);
        model.addAttribute("historyId",historyId);
        return "medical_history/member_medical_treatment_add_update";
    }

    @PostMapping("/save")
    public String doSaveMedicalTreatment(
            @ModelAttribute("memberId") long memberId,
            @ModelAttribute("historyId") long historyId,
            @ModelAttribute("medicalTreatment") @Validated PhMedicalTreatment medicalTreatment,
            BindingResult result){
        if(result.hasErrors()){
            return "medical_history/member_medical_treatment_add_update";
        }
        PhMedicalHistory history=historyService.findById(historyId);
        medicalTreatment.setMedicalHistory(history);
        treatmentService.save(medicalTreatment);
        return "redirect:/medicalTreatment?memberId="+memberId+"&historyId="+historyId;
    }
    @GetMapping("/delete")
    public String doDeleteMedicalTreatment(
            @RequestParam("id") long id,
            @RequestParam("historyId") long historyId,
            @RequestParam("memberId") long memberId){
        treatmentService.deleteById(id);
        return "redirect:/medicalTreatment?memberId="+memberId+"&historyId="+historyId;
    }
}
