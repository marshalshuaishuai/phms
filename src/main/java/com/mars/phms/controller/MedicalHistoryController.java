package com.mars.phms.controller;

import com.mars.phms.constant.Hint;
import com.mars.phms.constant.PhParam;
import com.mars.phms.domain.PhArea;
import com.mars.phms.domain.PhMedicalHistory;
import com.mars.phms.domain.PhMember;
import com.mars.phms.service.MedicalHistoryService;
import com.mars.phms.service.MemberService;
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
    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @GetMapping
    public String toMemberMedicalHistoryManagerPage(
            @ModelAttribute("medicalHistory") PhMedicalHistory medicalHistory,
            @RequestParam(name = "pageNum",defaultValue = "0") int pageNum,
            @RequestParam(name = "pageSize",defaultValue = PhParam.PAGE_DEFAULT_SIZE) int pageSize,
            HttpServletRequest request,
            Model model){
        //得到正在管理的成员，将姓名返回页面用于显示正在管理哪个成员，将ID返回页面用于下次查询时知道在管理哪个成员
        long id=Long.parseLong(request.getParameter("memberId"));
        PhMember member=memberService.findById(id);
        model.addAttribute("memberName",member.getName());
        model.addAttribute("memberId",id);
        //如果用户输入的每页显示数据条数大于最大值或小于最小值，设置为默认值
        if(pageSize<PhParam.PAGE_MIN_SIZE||pageSize>PhParam.PAGE_MAX_SIZE)
            pageSize=Integer.valueOf(PhParam.PAGE_DEFAULT_SIZE);
        //得到查询条件
        if(medicalHistory.getArea()!=null&&medicalHistory.getArea().getId()==-1){
            medicalHistory.setArea(null);
        }
        medicalHistory.setMember(member);
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("id");
        Example<PhMedicalHistory> medicalHistoryExample=Example.of(medicalHistory,matcher);
        //查询并将结果返回页面
        Page<PhMedicalHistory> medicalHistories=medicalHistoryService.findAllPaged(medicalHistoryExample,pageNum,pageSize);
        model.addAttribute("medicalHistories",medicalHistories);
        //将省份数据返回页面
        List<PhArea> provinces=areaService.getProvinces();
        model.addAttribute("provinces",provinces);
        if(medicalHistory.getArea()!=null&&medicalHistory.getArea().getParentId()!=-1){
            List<PhArea> cities=areaService.getCities(medicalHistory.getArea().getParentId());
            model.addAttribute("cities",cities);
        }
        //将每页查询记录条数返回页面
        model.addAttribute("pageSize",pageSize);
        return "/medical_history/member_medical_history_manager";
    }

    @GetMapping("/add")
    public String toMemberMedicalHistoryAddPage(
            @ModelAttribute("medicalHistory") PhMedicalHistory medicalHistory,
            @RequestParam("memberId") long memberId,
            Model model){
        List<PhArea> provinces=areaService.getProvinces();
        model.addAttribute("provinces",provinces);
        if(medicalHistory.getArea()!=null){
            List<PhArea> cities=areaService.getCities(medicalHistory.getArea().getParentId());
            model.addAttribute("cities",cities);
        }
        model.addAttribute("memberId",memberId);
        return "/medical_history/member_medical_history_add_update";
    }

    @GetMapping("/update")
    public String toMemberMedicalHistoryUpdatePage(
            @RequestParam("id") long id,
            @RequestParam("memberId") long memberId,
            Model model){
        PhMedicalHistory medicalHistory=medicalHistoryService.findById(id);
        model.addAttribute("medicalHistory",medicalHistory);
        model.addAttribute("memberId",memberId);
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
            @ModelAttribute("memberId") long memberId,
            @ModelAttribute("medicalHistory") @Validated PhMedicalHistory medicalHistory,
            BindingResult result,
            Model model){
        //确保用户选择了生病地点
        if(medicalHistory.getArea().getId()==-1){
            List<PhArea> provinces=areaService.getProvinces();
            model.addAttribute("provinces",provinces);
            if(medicalHistory.getArea().getParentId()!=-1){
                List<PhArea> cities=areaService.getCities(medicalHistory.getArea().getParentId());
                model.addAttribute("cities",cities);
            }
            model.addAttribute(Hint.AREA_ERROR,"请选择生病地点");
            return "/medical_history/member_medical_history_add_update";
        }
        if(result.hasErrors()){
            return "/medical_history/member_medical_history_add_update";
        }
        PhMember member=memberService.findById(memberId);
        medicalHistory.setMember(member);
        medicalHistoryService.save(medicalHistory);
        return "redirect:/medicalHistory?memberId="+memberId;
    }

    @GetMapping("/delete")
    public String doDeleteMedicalHistory(
            @RequestParam("id") long id,
            @RequestParam("memberId") long memberId){
        medicalHistoryService.deleteById(id);
        return "redirect:/medicalHistory?memberId="+memberId;
    }
}
