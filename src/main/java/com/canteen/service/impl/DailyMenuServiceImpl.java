package com.canteen.service.impl;

import com.canteen.entity.Dish;
import com.canteen.mapper.DishMapper;
import com.canteen.service.DailyMenuService;
import com.canteen.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 每日菜单服务实现类
 */
@Service
public class DailyMenuServiceImpl implements DailyMenuService {

    private final DishMapper dishMapper;
    private final Random random = new Random();

    public DailyMenuServiceImpl(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Override
    public List<DishVO> getTodayMenu() {
        // 1.调用dishMapper的selectAvailable方法，获取今日可用的菜品（状态为1的菜品）
        List<Dish> dishes = dishMapper.selectAvailable();
        // 2.将Dish对象转换为DishVO对象
        return dishes.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishVO> getTodayMenuByCategory(String category) {
        // 获取指定分类的今日菜品
        List<Dish> dishes = dishMapper.selectAvailableByCategory(category);
        return dishes.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 6 * * ?") // 每天早上6点执行
    public void updateDailyMenu() {
        // 1. 将所有菜品设置为停售状态（状态为0）
        dishMapper.updateAllStatus(0);
        
        // 2. 为每个分类随机选择菜品并设置为起售状态（状态为1）
        updateCategoryMenu("主食", 2, 3); // 主食选择2-3个
        updateCategoryMenu("荤菜", 3, 4); // 荤菜选择3-4个
        updateCategoryMenu("素菜", 2, 3); // 素菜选择2-3个
        updateCategoryMenu("汤类", 1, 2); // 汤类选择1-2个
    }

    /**
     * 更新指定分类的菜单
     */
    private void updateCategoryMenu(String category, int minCount, int maxCount) {
        // 获取该分类的所有菜品（包括停售的）
        List<Dish> categoryDishes = dishMapper.selectByCategory(category);
        
        if (categoryDishes.isEmpty()) {
            return;
        }
        
        // 随机选择菜品数量
        int selectCount = random.nextInt(maxCount - minCount + 1) + minCount;
        selectCount = Math.min(selectCount, categoryDishes.size());
        
        // 随机选择菜品并设置为起售状态
        for (int i = 0; i < selectCount; i++) {
            int randomIndex = random.nextInt(categoryDishes.size());
            Dish selectedDish = categoryDishes.get(randomIndex);
            
            // 设置为起售状态
            dishMapper.updateStatus(selectedDish.getId(), 1);
            
            // 从候选列表中移除已选择的菜品
            categoryDishes.remove(randomIndex);
        }
    }

    /**
     * 转换为VO对象
     */
    private DishVO convertToVO(Dish dish) {
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setStatusDesc(dish.getStatus() == 1 ? "起售" : "停售");
        return dishVO;
    }
} 