/*
 * @Author: mars
 * @Date: 2020-03-28 10:42:43
 * @Last Modified by: mars
 * @Last Modified time: 2020-03-28 16:00:06
 */
package com.mars.phms.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
@Component
public class PhAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 根据用户所属不同角色 登录成功后跳转到不同的页面
        String targetUrl = determineTargetUrl(authentication);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * 获取目标url
     *
     * @param authentication
     * @return
     */
    private String determineTargetUrl(Authentication authentication) {
        String url = "";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        if (isAdmin(roles)) {
            url = "/admin";
        } else if (isUser(roles)) {
            url = "/";
        } else {
            url = "/account/accessDenied";
        }
        return url;
    }

    /**
     * 判断用户是否为 user
     *
     * @param roles
     * @return
     */
    private boolean isUser(List<String> roles) {
        return roles.contains("ROLE_USER");
    }

    /**
     * 判断用户是否为 admin
     *
     * @param roles
     * @return
     */
    private boolean isAdmin(List<String> roles) {
        return roles.contains("ROLE_ADMIN");
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

}