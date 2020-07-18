package com.sxd.springcloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxd.springcloud.entities.Student;
import org.springframework.stereotype.Component;

@Component
public interface StudentMapper extends BaseMapper<Student> {
}
