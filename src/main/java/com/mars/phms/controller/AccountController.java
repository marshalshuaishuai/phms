/*
 * @Author: mars
 * @Date: 2020-03-28 08:50:01
 * @Last Modified by: mars
 * @Last Modified time: 2020-04-04 17:55:08
 */
package com.mars.phms.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mars.phms.constant.Hint;
import com.mars.phms.domain.PhArea;
import com.mars.phms.domain.PhMember;
import com.mars.phms.domain.PhUser;
import com.mars.phms.service.MemberService;
import com.mars.phms.service.UserService;
import com.mars.phms.utils.email.EmailService;
import com.mars.phms.utils.validatecode.ValidateCode;
import com.mars.phms.utils.validatecode.ValidateCodeCreateService;

import com.mars.phms.vo.UserRegisterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Controller
@RequestMapping("/account")
public class AccountController extends PhBaseController {
    @Autowired
    private ValidateCodeCreateService validateCodeService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MemberService memberService;

    /**
     * 用户注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String toRegisterPage(@ModelAttribute("user") PhUser user) {
        return "/account/register";
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String doRegister(@ModelAttribute("user") @Validated PhUser user, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "/account/register";
        }
        String confirmPassword = request.getParameter("confirm_password");
        if (!StringUtils.equals(user.getPassword(), confirmPassword)) {
            request.setAttribute(Hint.CONFIRM_PASSWORD_ERROR, "两次密码不一致");
            return "/account/register";
        }
        if (!isValidateCodeValid(request))
            return "/account/register";
        user.setRegistDay(new Date());
        PhUser savedUser=userService.saveUser(user);
        //注册成功后将自己加入成员中
        PhMember member=new PhMember();
        member.setUser(savedUser);
        member.setName("本人");
        member.setBirthday(new Date());
        memberService.saveMember(member);
        return "/account/login";
    }

    /**
     * 判断用户输入的验证码是否正确
     * @param request
     * @return
     */
    private boolean isValidateCodeValid(HttpServletRequest request) {
        String codeInRequest = request.getParameter("validate_code");
        ValidateCode codeInSession = (ValidateCode) request.getSession().getAttribute("validate_code");

        if (StringUtils.isEmpty(codeInRequest)) {
            request.setAttribute(Hint.VALIDATE_CODE_ERROR, "验证码不能为空");
            return false;
        }
        if (codeInSession == null) {
            request.setAttribute(Hint.VALIDATE_CODE_ERROR, "验证码不存在");
            return false;
        }
        if (codeInSession.isExpired()) {
            request.setAttribute(Hint.VALIDATE_CODE_ERROR, "验证码已过期");
            return false;
        }
        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            request.setAttribute(Hint.VALIDATE_CODE_ERROR, "验证码不匹配");
            return false;
        }
        request.getSession().removeAttribute("validate_code");
        return true;
    }


    /**
     * 登录
     *
     * @return
     */
    @GetMapping("/login")
    public String toLoginPage() {
        return "/account/login";
    }

    /**
     * 登出
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/account/login";
    }

    /**
     * 用户注册信息更新页面
     *
     * @param model
     * @return
     */
    @GetMapping("/userInfo")
    public String toUserInfo(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserRegisterInfo registerInfo = userService.getUserBasicInfo(username);
        model.addAttribute("registerInfo", registerInfo);

        return "/account/user_info";
    }

    /**
     * 用户注册信息更新
     *
     * @param userinfo
     * @return
     */
    @PostMapping("/saveUserInfo")
    public String saveUserInfo(@ModelAttribute("user") UserRegisterInfo userinfo) {
        userService.updateUserBasicInfo(userinfo);
        return "redirect:/account/userInfo";
    }


    /**
     * 返回验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/get-validate-code")
    public void getValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ValidateCode validateCode = validateCodeService.createImageCode();
        request.getSession().setAttribute("validate_code", validateCode);
        ImageIO.write(validateCode.getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * 通过邮件发送验证码
     * @param request
     * @return
     */
    @GetMapping("/send-validate-code")
    @ResponseBody
    public String sendValidateCode(HttpServletRequest request) {
        try {
            String to = request.getParameter("to");
            if(userService.isEmailUsed(to)){
                return "该邮箱已被占用，请更换后重试";
            }
            ValidateCode validateCode = validateCodeService.createImageCode();
            request.getSession().setAttribute("validate_code", validateCode);
            emailService.sendValidateCode(to, validateCode.getCode());
            return "验证码已成功发送到："+to;
        } catch (Exception e) {
            return "验证码发送失败，请重试";
        }
    }

    /**
     * 权限不足页面
     *
     * @return
     */
    @GetMapping("/accessDenied")
    public String toAccessDeniedPage() {
        return "/account/accessDenied";
    }

    /**
     * 修改密码
     *
     * @return
     */
    @GetMapping("/changePassword")
    public String toChangePasswordPage() {
        return "/account/change_password";
    }

    @PostMapping("/changePassword")
    public String doChangePassword(HttpServletRequest request) {
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        if (StringUtils.isEmpty(oldPassword)) {
            request.setAttribute(Hint.CHANGE_PASSWORD_ERROR, "请输入原密码");
        } else if (StringUtils.isEmpty(newPassword)) {
            request.setAttribute(Hint.CHANGE_PASSWORD_ERROR, "请输入新密码");
        } else if (!StringUtils.equals(newPassword, confirmPassword)) {
            request.setAttribute(Hint.CHANGE_PASSWORD_ERROR, "两次密码不一致");
        } else {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if (userService.changeUserPassword(username, oldPassword, newPassword)) {
                request.setAttribute(Hint.CHANGED_PASSWORD_SUCCESS, "密码修改成功");
            } else {
                request.setAttribute(Hint.CHANGE_PASSWORD_ERROR, "原密码不正确");
            }
        }
        return "/account/change_password";
    }

    /**
     * 变更邮箱
     * @return
     */
    @GetMapping("/changeEmail")
    public String toChangeEmailPage(Model model){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        PhUser user=userService.findByName(username);
        model.addAttribute("email",user.getEmail());
        return "/account/change_email";
    }

    @PostMapping("/changeEmail")
    public String doChangeEmail(HttpServletRequest request){
        String email=request.getParameter("email");
        String email_old=request.getParameter("old_email");
        request.setAttribute("email",email_old);
        if(StringUtils.isEmpty(email)){
            request.setAttribute(Hint.EMAIL_ERROR,"请输入新邮箱地址");
            return "/account/change_email";
        }
        if(!isValidateCodeValid(request)){
            return "/account/change_email";
        }
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        userService.changeEmail(username,email);
        return "redirect:/account/changeEmail";
    }
}