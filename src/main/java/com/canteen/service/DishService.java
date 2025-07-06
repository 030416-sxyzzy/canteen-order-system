package com.canteen.service;

import com.canteen.dto.DishDTO;
import com.canteen.vo.DishVO;
import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO 菜品信息
     * @return 菜品ID
     */
    Long save(DishDTO dishDTO);

    /**
     * 更新菜品
     * @param id 菜品ID
     * @param dishDTO 菜品信息
     */
    void update(Long id, DishDTO dishDTO);

    /**
     * 删除菜品
     * @param id 菜品ID
     */
    void delete(Long id);

    /**
     * 根据ID查询菜品
     * @param id 菜品ID
     * @return 菜品信息
     */
    DishVO getById(Long id);

    /**
     * 查询所有菜品
     * @return 菜品列表
     */
    List<DishVO> list();

    /**
     * 获取菜单（所有上架状态的菜品）
     * @return 菜单列表
     */
    List<DishVO> getMenu();

    /**
     * 根据分类获取菜单
     * @param category 菜品分类
     * @return 菜单列表
     */
    List<DishVO> getMenuByCategory(String category);

    /**
     * 更新菜单（根据菜品状态自动更新）
     * @return 更新后的菜单数量
     */
    int updateMenu();
} 