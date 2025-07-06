package com.canteen.dto;

import lombok.Data;

/**
 * 菜品数据传输对象
 */
@Data
public class DishDTO {
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
} 