package com.sxd.springcloud.security;

import com.alibaba.fastjson.JSON;
import com.sxd.springcloud.common.Enums.ResultEnum;
import com.sxd.springcloud.common.VO.ResultVO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**

 * @description: 无权访问
 */
@Component
public class AjaxAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //解决乱码
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.USER_NO_ACCESS,false)));
    }
}
