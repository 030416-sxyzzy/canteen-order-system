package com.canteen.vo;

import lombok.Data;

/**
 * 菜品视图对象
 */
@Data
public class DishVO {
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
     * 菜品状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private String createTime;
} 