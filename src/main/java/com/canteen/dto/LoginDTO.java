package com.canteen.dto;

import lombok.Data;

/**
 * 登录数据传输对象
 */
@Data
public class LoginDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型 1-学生 2-管理员
     */
    private Integer userType;
} 