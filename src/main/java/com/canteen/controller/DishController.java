package com.canteen.controller;

import com.canteen.common.Result;
import com.canteen.dto.DishDTO;
import com.canteen.service.DishService;
import com.canteen.vo.DishVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品控制器
 */
@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private static final Logger log = LoggerFactory.getLogger(DishController.class);

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * 新增菜品
     */
    @PostMapping
    public Result<Long> save(@RequestBody DishDTO dishDTO) {
        Long dishId = dishService.save(dishDTO);
        return Result.success(dishId);
    }

    /**
     * 更新菜品
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody DishDTO dishDTO) {
        dishService.update(id, dishDTO);
        return Result.success();
    }

    /**
     * 删除菜品
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        dishService.delete(id);
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 获取所有菜品
     */
    @GetMapping
    public Result<List<DishVO>> list() {
        log.info("获取所有菜品");
        List<DishVO> dishVOList = dishService.list();
        return Result.success(dishVOList);
    }

    /**
     * 获取菜单（所有上架状态的菜品）
     */
    @GetMapping("/menu")
    public Result<List<DishVO>> getMenu() {
        log.info("获取菜单");
        List<DishVO> menuList = dishService.getMenu();
        return Result.success(menuList);
    }

    /**
     * 根据分类获取菜单
     */
    @GetMapping("/menu/{category}")
    public Result<List<DishVO>> getMenuByCategory(@PathVariable String category) {
        log.info("根据分类获取菜单：{}", category);
        List<DishVO> menuList = dishService.getMenuByCategory(category);
        return Result.success(menuList);
    }

    /**
     * 更新菜单
     */
    @PostMapping("/menu/update")
    public Result<Integer> updateMenu() {
        log.info("更新菜单");
        int menuCount = dishService.updateMenu();
        return Result.success(menuCount);
    }
} 