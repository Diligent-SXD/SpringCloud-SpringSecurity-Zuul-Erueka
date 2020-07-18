package com.sxd.springcloud.common.security;

import com.alibaba.fastjson.JSON;
import com.sxd.springcloud.common.Enums.ResultEnum;
import com.sxd.springcloud.common.VO.ResultVO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 用户登录失败时返回给前端的数据
 */
@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //解决乱码
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_LOGIN_FAILED,false)));
    }

}
