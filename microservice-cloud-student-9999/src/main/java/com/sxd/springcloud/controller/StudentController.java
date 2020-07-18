package com.sxd.springcloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxd.springcloud.common.Enums.ResultEnum;
import com.sxd.springcloud.common.VO.ResultVO;
import com.sxd.springcloud.dao.StudentMapper;
import com.sxd.springcloud.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class StudentController {
    @Autowired
    private StudentMapper studentMapper;

    @PostMapping("/list")
    public Map<String, Object> getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        IPage<Student> userPage = new Page<>(pageNum, pageSize);//参数一是当前页，参数二是每页个数
        userPage = studentMapper.selectPage(userPage, null);
        List<Student> list = userPage.getRecords();
        return ResultVO.result(ResultEnum.SUCCESS, list, true);
    }

    //增
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public Map<String, Object> addInfo(@RequestParam Map<String, String> map){
        Student s = new Student();
        s.setName(map.get("name"));
        s.setAddress(map.get("address"));
        s.setSex(map.get("sex"));
        s.setHeight(Double.parseDouble(map.get("height")));
        int flag = studentMapper.insert(s);
        if(flag == 1){
            return ResultVO.success(ResultEnum.SUCCESS, "添加成功", true);
        }else{
            return ResultVO.failure(ResultEnum.FAILURE, "添加失败", false);
        }
    }

    //删
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteInfo(@PathVariable long id){
        int flag = studentMapper.deleteById(id);
        if(flag == 1){
            return ResultVO.success(ResultEnum.SUCCESS, "删除成功", true);
        }else{
            return ResultVO.failure(ResultEnum.FAILURE, "删除失败", false);
        }
    }
    //改
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public Map<String, Object> update( @PathVariable(value = "id") Long id, @RequestBody Student stu){
        Student s = new Student();
        s.setId(id);
        s.setName(stu.getName());
        s.setAddress(stu.getAddress());
        s.setSex(stu.getSex());
        s.setHeight(stu.getHeight());
        int flag = studentMapper.updateById(s);
        if(flag == 1){
            return ResultVO.success(ResultEnum.SUCCESS, "更新成功", true);
        }else{
            return ResultVO.failure(ResultEnum.FAILURE, "更新失败", false);
        }
    }
}
