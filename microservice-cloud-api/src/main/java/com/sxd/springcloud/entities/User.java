package com.sxd.springcloud.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @description: User实体类
 */

@Data
@NoArgsConstructor
@TableName("User")
public class User {

    @TableId
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String role;

    private int status;

}
