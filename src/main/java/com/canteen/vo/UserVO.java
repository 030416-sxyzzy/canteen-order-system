package com.canteen.vo;

import lombok.Data;

/**
 * 用户视图对象
 */
@Data
public class UserVO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户类型 1-学生 2-管理员
     */
    private Integer userType;

    /**
     * 用户类型描述
     */
    private String userTypeDesc;
} 