package com.mars.phms.controller;

import com.mars.phms.domain.PhArea;
import com.mars.phms.domain.PhMember;
import com.mars.phms.domain.PhUser;
import com.mars.phms.service.MemberService;
import com.mars.phms.vo.MemberSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    @GetMapping("/memberManager")
    public String toMemberManagerPage(@ModelAttribute("member") PhMember member, Model model){
        PhUser user = (PhUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        member.setUser(user);
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("height")
                .withIgnorePaths("weight")
                .withIgnorePaths("id");
        Example<PhMember> memberExample=Example.of(member,matcher);
        List<PhMember> members=memberService.findAll(memberExample);
        //List<PhMember> members=memberService.findByUserId(user.getId());
        model.addAttribute("members",members);
        return "/member_manager";
    }

    @GetMapping("/memberAdd")
    public String toAddMemberPage(@ModelAttribute("member") PhMember member, Model model){
        List<PhArea> provinces = areaService.getProvinces();
        model.addAttribute("provinces",provinces);
        return "/member";
    }
    @GetMapping("/memberUpdate")
    public String toUpdateMemberPage( Model model, @RequestParam(name = "id") Long id){
        List<PhArea> provinces = areaService.getProvinces();
        PhMember member=memberService.findById(id);
        if(member.getArea()!=null){
            List<PhArea> cities=areaService.getCities(member.getArea().getParentId());
            model.addAttribute("cities",cities);
        }
        model.addAttribute("provinces",provinces);
        model.addAttribute("member",member);
        return "/member";
    }
    @PostMapping("/member")
    public String doSaveMember(@ModelAttribute("member") @Validated PhMember member,BindingResult result){
        if(result.hasErrors()){
            return "/member";
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
}