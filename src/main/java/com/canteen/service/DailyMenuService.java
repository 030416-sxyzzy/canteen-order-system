package com.canteen.service;

import com.canteen.vo.DishVO;
import java.util.List;

/**
 * 每日菜单服务接口
 */
public interface DailyMenuService {
    
    /**
     * 获取今日菜单
     */
    List<DishVO> getTodayMenu();
    
    /**
     * 自动更新每日菜单
     */
    void updateDailyMenu();
    
    /**
     * 获取指定分类的今日菜品
     */
    List<DishVO> getTodayMenuByCategory(String category);
} 