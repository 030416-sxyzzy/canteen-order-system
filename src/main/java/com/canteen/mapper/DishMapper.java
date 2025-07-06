package com.canteen.mapper;

import com.canteen.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 菜品Mapper接口
 */
@Mapper
public interface DishMapper {
    /**
     * 新增菜品
     * @param dish 菜品信息
     */
    void insert(Dish dish);

    /**
     * 更新菜品
     * @param dish 菜品信息
     */
    void update(Dish dish);

    /**
     * 根据ID删除菜品
     * @param id 菜品ID
     */
    void deleteById(Long id);

    /**
     * 根据ID查询菜品
     * @param id 菜品ID
     * @return 菜品信息
     */
    Dish selectById(Long id);

    /**
     * 查询所有菜品
     * @return 菜品列表
     */
    List<Dish> selectAll();

    /**
     * 查询所有上架状态的菜品
     * @return 菜品列表
     */
    List<Dish> selectAvailable();

    /**
     * 根据分类查询上架状态的菜品
     * @param category 菜品分类
     * @return 菜品列表
     */
    List<Dish> selectAvailableByCategory(String category);
    
    // 根据分类查询菜品
    List<Dish> selectByCategory(String category);
    
    // 根据状态查询菜品
    List<Dish> selectByStatus(Integer status);
    
    /**
     * 批量更新所有菜品状态
     * @param status 状态值
     */
    void updateAllStatus(Integer status);
    
    /**
     * 更新指定菜品的状态
     * @param id 菜品ID
     * @param status 状态值
     */
    void updateStatus(Long id, Integer status);
} 