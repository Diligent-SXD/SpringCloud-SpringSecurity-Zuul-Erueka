package com.sxd.springcloud.config;

import com.sxd.springcloud.filters.JwtAuthenticationTokenFilter;
import com.sxd.springcloud.security.AjaxAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * 〈Security配置〉
 * Security配置
 * @author wangmx
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@Order(99)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter; // JWT 拦截器

    @Autowired
    AjaxAccessDeniedHandler accessDeniedHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous()
                .and()
                .exceptionHandling().authenticationEntryPoint(
                (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/student/**").permitAll()
                .anyRequest().authenticated();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler); // 无权访问 JSON 格式的数据
    }
}
