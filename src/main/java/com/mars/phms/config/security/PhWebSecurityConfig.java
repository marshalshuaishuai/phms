/*
 * @Author: mars
 * @Date: 2020-03-28 08:50:37
 * @Last Modified by: mars
 * @Last Modified time: 2020-04-01 10:05:48
 */
package com.mars.phms.config.security;

import com.mars.phms.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class PhWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PhAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private UserService userService;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    private AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * 验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置自定义验证
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * 授权
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        PhValidateCodeFilter validateCodeFilter = new PhValidateCodeFilter();
        //取消限制IFRAME使用
        http.headers().frameOptions().disable();
        // 自定义授权规则
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class).authorizeRequests()
                .antMatchers("/account/login", "/login", "/account/register",
                        "/account/getValidateCode","/account/sendValidateCode", "/css/**",
                        "/js/**", "/images/**", "/lib/**")
                .permitAll().antMatchers("/", "/home").hasRole("USER").antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().formLogin().loginPage("/account/login").loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler).permitAll().and().rememberMe()
                .tokenValiditySeconds(60 * 60 * 24)// 默认两周，此处设置为24小时
                .userDetailsService(userService)// 自定义userservice，如果没有自定义的，应该可以不要这句
                .and().logout().logoutSuccessUrl("/account/login").invalidateHttpSession(true).permitAll().and()
                .exceptionHandling().accessDeniedPage("/account/accessDenied");
        http.csrf().disable();
    }

}