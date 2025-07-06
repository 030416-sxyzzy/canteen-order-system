package com.canteen.entity;

import lombok.Data;

/**
 * 菜品实体类
 */
@Data
public class Dish {
    /**
     * 菜品ID
     */
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品价格
     */
    private Double price;

    /**
     * 菜品描述
     */
    private String description;

    /**
     * 菜品分类
     */
    private String category;

    /**
     * 菜品状态 0-停售 1-起售
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
} 