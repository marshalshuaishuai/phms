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

import com.mars.phms.domain.PhArea;
import com.mars.phms.domain.PhUser;
import com.mars.phms.service.AreaService;
import com.mars.phms.service.UserService;
import com.mars.phms.utils.ValidateCode.ValidateCode;
import com.mars.phms.utils.ValidateCode.ValidateCodeCreateService;

import com.mars.phms.vo.UserBasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Controller
@RequestMapping("/account")
public class AccountController {
    private static final String VALIDATE_CODE_ERROR = "validate_code_error";
    private static final String CONFIRM_PASSWORD_ERROR="confirm_password_error";
    @Autowired
    private ValidateCodeCreateService validateCodeService;

    @Autowired
    private UserService userService;
    @Autowired
    private AreaService areaService;

    /**
     * 用户注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String toRegisterPage(@ModelAttribute("RegistInfo") PhUser user) {
        return "/account/register";
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String doRegister(@ModelAttribute("RegistInfo") @Validated PhUser user, BindingResult result,HttpServletRequest request) {
        if (result.hasErrors()) {
            return "/account/register";
        }
        String confirmPassword=request.getParameter("confirm_password");
        if(!StringUtils.equals(user.getPassword(), confirmPassword)){
            request.setAttribute(CONFIRM_PASSWORD_ERROR, "两次密码不一致");
            return "/account/register";
        }
        String codeInRequest = request.getParameter("validate_code");
        ValidateCode codeInSession = (ValidateCode) request.getSession().getAttribute("validate_code");

        if (StringUtils.isEmpty(codeInRequest)) {
            request.setAttribute(VALIDATE_CODE_ERROR, "验证码不能为空");
            return "/account/register";
        }
        if (codeInSession == null) {
            request.setAttribute(VALIDATE_CODE_ERROR, "验证码不存在");
            return "/account/register";
        }
        if (codeInSession.isExpired()) {
            request.setAttribute(VALIDATE_CODE_ERROR, "验证码已过期");
            return "/account/register";
        }
        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            request.setAttribute(VALIDATE_CODE_ERROR, "验证码不匹配");
            return "/account/register";
        }
        request.getSession().removeAttribute("validate_code");
        user.setRegistDay(new Date());
        user.setSex(1);
        userService.saveUser(user);
        return "/account/login";
        // return user == null ? "注册失败" : "注册成功";
    }


    /**
     * 登录
     * @return
     */
    @GetMapping("/login")
    public String toLoginPage(){
        return "/account/login";
    }

    /**
     * 登出
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/account/login";
    }

    /**
     * 用户基本信息更新
     * @param model
     * @return
     */
    @GetMapping("/userInfo")
    public String toUserInfo(Model model) {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        UserBasicInfo userBasicInfo=userService.getUserBasicInfo(username);
        model.addAttribute("userBasicInfo", userBasicInfo);
        List<PhArea> provinces = areaService.getProvinces();
        model.addAttribute("provinces", provinces);
        if (userBasicInfo.getAreaParentId() != null) {
            List<PhArea> cities = areaService.getCities(userBasicInfo.getAreaParentId());
            model.addAttribute("cities", cities);
        }
        return "/account/user_info";
    }

    /**
     * 用户基本信息更新
     * @param userinfo
     * @return
     */
    @PostMapping("/saveUserInfo")
    public String saveUserInfo(@ModelAttribute("user") UserBasicInfo userinfo){
        if(userinfo.getAreaId()==-1)
            userinfo.setAreaId(null);
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
     * 权限不足页面
     * @return
     */
    @GetMapping("/accessDenied")
    public String toAccessDeniedPage() {
        return "/account/accessDenied";
    }
}