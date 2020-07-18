package com.sxd.springcloud.common.filters;

import com.alibaba.fastjson.JSON;
import com.sxd.springcloud.common.Enums.ResultEnum;
import com.sxd.springcloud.common.VO.ResultVO;
import com.sxd.springcloud.common.utils.DateUtil;
import com.sxd.springcloud.common.utils.JwtTokenUtil;
import com.sxd.springcloud.common.utils.RedisUtil;
import com.sxd.springcloud.demo.service.SelfUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 确保在一次请求只通过一次filter，而不需要重复执行
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    SelfUserDetailsService userDetailsService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        String authHeader = request.getHeader("Authorization");


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String authToken = authHeader.substring("Bearer ".length());

            String username = JwtTokenUtil.parseToken(authToken);
            if(StringUtils.isEmpty(username)){
                response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.LOGIN_IS_OVERDUE, false)));
                return;
            }
            if (redisUtil.hasKey(username)) {//判断redis是否有保存
                String expirationTime = redisUtil.hget(username,"expirationTime").toString();
                if (JwtTokenUtil.isExpiration(expirationTime)) {
                    //获得redis中用户的token刷新时效
                    String tokenValidTime = (String) redisUtil.getTokenValidTimeByToken(username);
                    String currentTime = DateUtil.getTime();

                    if (DateUtil.compareDate(currentTime, tokenValidTime)) {
                        //超过有效期，不予刷新
                        log.info("{}已超过有效期，不予刷新",authToken);
                        response.getWriter().write(JSON.toJSONString(ResultVO.result(ResultEnum.LOGIN_IS_OVERDUE, false)));
                        return;
                    } else {
                        //自定义身份信息
                        Map<String, Object> map = new HashMap<>();
                        String jwtToken = JwtTokenUtil.generateToken(username, expirationSeconds, map);

                        //删除旧的token保存的redis
                        redisUtil.deleteKey(username);

                        redisUtil.setTokenRefresh(username,jwtToken);

                        log.info("redis已删除旧token：{},新token：{}已更新redis",authToken,jwtToken);
                        authToken = jwtToken;//更新token，为了后面
                        response.setHeader("Authorization", "Bearer " + jwtToken);
                    }
                }

            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
