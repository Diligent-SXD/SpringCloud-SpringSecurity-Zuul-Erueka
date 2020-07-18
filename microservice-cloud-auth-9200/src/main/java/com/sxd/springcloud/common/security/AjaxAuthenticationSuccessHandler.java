package com.sxd.springcloud.common.security;

import com.alibaba.fastjson.JSON;
import com.sxd.springcloud.common.Enums.ResultEnum;
import com.sxd.springcloud.common.VO.ResultVO;
import com.sxd.springcloud.common.utils.JwtTokenUtil;
import com.sxd.springcloud.common.utils.RedisUtil;
import com.sxd.springcloud.entities.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 用户登录成功时返回给前端的数据
 */
@Component
@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Map<String,Object> map = new HashMap<>();

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        map.put("role", myUserDetails.getAuthorities());
        String jwtToken = JwtTokenUtil.generateToken(myUserDetails.getUsername(), expirationSeconds, map);

        //redis中有该用户信息
        if (redisUtil.hasKey(myUserDetails.getUsername())) {//判断redis是否有保存
           //把旧的删除再重新保存
            redisUtil.deleteKey(myUserDetails.getUsername());
        }
        //刷新时间
        Integer expire = validTime*24*60*60*1000;

        redisUtil.setTokenRefresh(myUserDetails.getUsername(), jwtToken);
        log.info("用户{}登录成功，信息已保存至redis", myUserDetails.getUsername());
        //解决乱码
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGIN_SUCCESS,jwtToken,true)));
    }
}
