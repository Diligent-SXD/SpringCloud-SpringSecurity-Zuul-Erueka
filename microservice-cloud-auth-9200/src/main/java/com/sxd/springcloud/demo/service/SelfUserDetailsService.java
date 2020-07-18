package com.sxd.springcloud.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sxd.springcloud.demo.dao.UserMapper;
import com.sxd.springcloud.entities.MyUserDetails;
import com.sxd.springcloud.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 用户认证、权限
 */
@Component
public class SelfUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //通过username查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        List<User> userInfoList = userMapper.selectList(queryWrapper);
        if(userInfoList.size() == 0){
            throw new UsernameNotFoundException("用户" + username + "不存在");
        }
        return new MyUserDetails(userInfoList.get(0));
    }
}
