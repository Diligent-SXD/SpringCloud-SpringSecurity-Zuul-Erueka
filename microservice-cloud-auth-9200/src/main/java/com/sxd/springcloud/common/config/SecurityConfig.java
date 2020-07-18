package com.sxd.springcloud.common.config;

import com.sxd.springcloud.common.filters.JwtAuthenticationTokenFilter;
import com.sxd.springcloud.common.security.*;
import com.sxd.springcloud.demo.service.SelfUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @description: Security配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    AjaxAuthenticationEntryPoint authenticationEntryPoint;//未登陆时返回 JSON 格式的数据给前端（否则为 html）

    @Autowired
    AjaxAuthenticationSuccessHandler authenticationSuccessHandler; //登录成功返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    AjaxAuthenticationFailureHandler authenticationFailureHandler; //登录失败返回的 JSON 格式数据给前端（否则为 html）

    @Autowired
    AjaxLogoutSuccessHandler logoutSuccessHandler;//注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）

    @Autowired
    AjaxAccessDeniedHandler accessDeniedHandler;//无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）

    @Autowired
    SelfUserDetailsService userDetailsService; // 自定义user

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter; // JWT 拦截器

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加入自定义的安全认证
//        auth.authenticationProvider(provider);
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 去掉 CSRF
        http.csrf().disable()
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 使用 JWT，关闭token
                .and()

                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)

                .and()
                .authorizeRequests()//定义哪些URL需要被保护、哪些不需要被保护

                .anyRequest()//任何请求,登录后可以访问
                //这一句相当于把.antMatchers("/auth/tasks/**").authenticated()都放在一个文件夹里面了
                .access("@rbacauthorityservice.hasPermission(request,authentication)") // RBAC 动态 url 认证

                .and()
                .formLogin()  //开启登录, 定义当需要用户登录时候，转到的登录页面
                .successHandler(authenticationSuccessHandler) // 登录成功
                .failureHandler(authenticationFailureHandler) // 登录失败
                .permitAll()

                .and()
                .logout()//默认注销行为为logout
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler); // 无权访问 JSON 格式的数据
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class); // JWT Filter

    }
}
