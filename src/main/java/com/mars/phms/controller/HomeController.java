package com.mars.phms.controller;

import com.mars.phms.constant.PhParam;
import com.mars.phms.domain.PhArea;
import com.mars.phms.domain.PhMember;
import com.mars.phms.domain.PhUser;
import com.mars.phms.service.MemberService;
import com.mars.phms.vo.MemberRecentDiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class HomeController extends PhBaseController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String toHomePage(Model model) {
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", loggedInUserName);
        return "home";
    }


    /**
     * 成员管理
     * @param member
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/memberManager")
    public String toMemberManagerPage(
            //双向绑定模型参数，用于指定查询条件
            @ModelAttribute("member") PhMember member,
            //分页参数
            @RequestParam(value = "pageNum",defaultValue = "0") int pageNum,
            @RequestParam(value = "pageSize",defaultValue =PhParam.PAGE_DEFAULT_SIZE) int pageSize,
            //返回页面的模型
            Model model){
        //得到当前用户
        PhUser user = (PhUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //设置搜索条件
        member.setUser(user);
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("id");
        Example<PhMember> memberExample=Example.of(member,matcher);
        //如果用户输入的每页显示数据条数大于最大值或小于最小值，设置为默认值
        if(pageSize<PhParam.PAGE_MIN_SIZE||pageSize>PhParam.PAGE_MAX_SIZE)
            pageSize=Integer.valueOf(PhParam.PAGE_DEFAULT_SIZE);
        //分页查找符合条件的记录
        Page<PhMember> members=memberService.findAllPaged(memberExample,pageNum,pageSize);
        //将查找到的记录返回页面
        model.addAttribute("members",members);
        //将每页查询记录条数返回页面
        model.addAttribute("pageSize",pageSize);
        return "/member_manager";
    }

    @GetMapping("/memberAdd")
    public String toAddMemberPage(@ModelAttribute("member") PhMember member, Model model){
        List<PhArea> provinces = areaService.getProvinces();
        model.addAttribute("provinces",provinces);
        return "member_add_update";
    }
    @GetMapping("/memberUpdate")
    public String toUpdateMemberPage(@RequestParam(name = "id") Long id, Model model){
        List<PhArea> provinces = areaService.getProvinces();
        PhMember member=memberService.findById(id);
        if(member.getArea()!=null){
            List<PhArea> cities=areaService.getCities(member.getArea().getParentId());
            model.addAttribute("cities",cities);
        }
        model.addAttribute("provinces",provinces);
        model.addAttribute("member",member);
        return "member_add_update";
    }
    @PostMapping("/member")
    public String doSaveMember(@ModelAttribute("member") @Validated PhMember member,BindingResult result){
        if(result.hasErrors()){
            return "member_add_update";
        }
        PhUser user= (PhUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        member.setUser(user);
        if(member.getArea().getId()==-1)
            member.setArea(null);
        memberService.saveMember(member);
        return "redirect:/memberManager";
    }
    @GetMapping("/memberDelete")
    public String doDeleteMember(@RequestParam("id") long id){
        memberService.deleteMember(id);
        return "redirect:/memberManager";
    }

    /**
     * 成员最近病史管理页面
     * @param model
     * @return
     */
    @GetMapping("memberMedicalHistoryIndex")
    public String toMemberMedicalHistoryIndexPage(Model model){
        String loggedInUserName=SecurityContextHolder.getContext().getAuthentication().getName();
        List<MemberRecentDiseaseVo> memberRecentDiseaseVos =memberService.findRecentDiseaseForMember(loggedInUserName);
        model.addAttribute("memberRecentDiseaseVos", memberRecentDiseaseVos);
        return "member_medical_history_index";
    }
}