package com.sxd.springcloud.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @description: Student实体类
 */

@Data
@NoArgsConstructor
@TableName("Student")
public class Student {
    @TableId
    private long id;
    private String name;
    private String sex;
    private double height;
    private String address;
}
