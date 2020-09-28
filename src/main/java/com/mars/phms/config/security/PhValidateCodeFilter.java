/*
 * @Author: mars
 * @Date: 2020-03-31 17:35:20
 * @Last Modified by: mars
 * @Last Modified time: 2020-03-31 17:39:40
 */
package com.mars.phms.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mars.phms.utils.validatecode.ValidateCode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;


/**
 * 使用方法
 * 在自定义的 继承自 WebSecurityConfigurerAdapter 的类中
 * 在 configure(HttpSecurity http) 方法中 加入以下代码即可
 * ValidateCodeFilter validateCodeFilter=new ValidateCodeFilter();
 * http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
 */
public class PhValidateCodeFilter extends OncePerRequestFilter {
    protected final Log logger=LogFactory.getLog(getClass());

    //此处应该有更好的方法 获得 系统配置中的 loginPage 替代 "/account/login?error" 留待以后解决
    // "/account/login" 为系统设置的登录页
    private AuthenticationFailureHandler failureHandler=new SimpleUrlAuthenticationFailureHandler("/account/login?error");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(StringUtils.equals("/phms/login", request.getRequestURI())&&StringUtils.equalsIgnoreCase(request.getMethod(), "post")){
            try {
                validate(request);
            } catch (ValidateCodeException e) {
                failureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        doFilter(request, response, filterChain);

    }

    private void validate(HttpServletRequest request) {
        String codeInRequest=request.getParameter("validate_code");
        ValidateCode codeInSession= (ValidateCode) request.getSession().getAttribute("validate_code");

        if(StringUtils.isEmpty(codeInRequest)){
            throw new ValidateCodeException("请填写验证码");
        }
        if(codeInSession==null){
            throw new ValidateCodeException("验证码不存在");
        }
        if(codeInSession.isExpired()){
            throw new ValidateCodeException("验证码已过期");
        }
        if(!StringUtils.equals(codeInSession.getCode(), codeInRequest)){
            throw new ValidateCodeException("验证码不正确");
        }
        request.getSession().removeAttribute("validate_code");
    }

}

class ValidateCodeException extends AuthenticationException{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}