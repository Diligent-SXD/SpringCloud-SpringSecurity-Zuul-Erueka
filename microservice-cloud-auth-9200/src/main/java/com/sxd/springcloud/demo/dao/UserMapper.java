package com.sxd.springcloud.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxd.springcloud.entities.User;
import org.springframework.stereotype.Component;

/**
 * @description: 用户dao层
 */
@Component
public interface UserMapper extends BaseMapper<User> {

}
