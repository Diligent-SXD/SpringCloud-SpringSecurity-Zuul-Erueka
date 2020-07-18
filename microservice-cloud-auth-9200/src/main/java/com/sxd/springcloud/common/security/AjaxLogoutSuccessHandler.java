package com.sxd.springcloud.common.security;

import com.alibaba.fastjson.JSON;
import com.sxd.springcloud.common.Enums.ResultEnum;
import com.sxd.springcloud.common.VO.ResultVO;
import com.sxd.springcloud.common.utils.JwtTokenUtil;
import com.sxd.springcloud.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 登出成功
 */
@Component
@Slf4j
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring("Bearer ".length());
            String username = JwtTokenUtil.parseToken(authToken);
            redisUtil.deleteKey(username);
            log.info("用户登出成功！username：{}已从redis移除", username);
        }
        //解决乱码
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGOUT_SUCCESS,true)));
    }

}
