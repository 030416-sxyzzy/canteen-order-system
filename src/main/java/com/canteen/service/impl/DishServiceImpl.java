package com.canteen.service.impl;

import com.canteen.dto.DishDTO;
import com.canteen.entity.Dish;
import com.canteen.mapper.DishMapper;
import com.canteen.service.DishService;
import com.canteen.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品服务实现类
 */
@Service
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;

    public DishServiceImpl(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    @Override
    public Long save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        
        // 设置创建时间和更新时间
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dish.setCreateTime(now);
        dish.setUpdateTime(now);
        
        dishMapper.insert(dish);
        return dish.getId();
    }

    @Override
    public void update(Long id, DishDTO dishDTO) {
        // 检查菜品是否存在
        Dish existDish = dishMapper.selectById(id);
        if (existDish == null) {
            throw new RuntimeException("菜品不存在");
        }

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setId(id);
        dish.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        dishMapper.update(dish);
    }

    @Override
    public void delete(Long id) {
        dishMapper.deleteById(id);
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.selectById(id);
        return dish != null ? convertToVO(dish) : null;
    }

    @Override
    public List<DishVO> list() {
        List<Dish> dishes = dishMapper.selectAll();
        return dishes.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<DishVO> getMenu() {
        List<Dish> dishes = dishMapper.selectAvailable();
        return dishes.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<DishVO> getMenuByCategory(String category) {
        List<Dish> dishes = dishMapper.selectAvailableByCategory(category);
        return dishes.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public int updateMenu() {
        // 菜单更新就是获取当前上架状态的菜品数量
        List<Dish> availableDishes = dishMapper.selectAvailable();
        return availableDishes.size();
    }

    /**
     * 将Dish对象转换为DishVO对象
     */
    private DishVO convertToVO(Dish dish) {
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        // 设置状态描述
        dishVO.setStatusDesc(dish.getStatus() == 1 ? "起售" : "停售");
        return dishVO;
    }
} 