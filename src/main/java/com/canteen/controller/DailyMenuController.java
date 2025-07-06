package com.canteen.controller;

import com.canteen.common.Result;
import com.canteen.service.DailyMenuService;
import com.canteen.vo.DishVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 每日菜单控制器
 */
@RestController
@RequestMapping("/api/daily-menu")
public class DailyMenuController {

    private final DailyMenuService dailyMenuService;

    public DailyMenuController(DailyMenuService dailyMenuService) {
        this.dailyMenuService = dailyMenuService;
    }

    /**
     * 获取今日菜单
     */
    @GetMapping
    public Result<List<DishVO>> getTodayMenu() {
        List<DishVO> menu = dailyMenuService.getTodayMenu();
        return Result.success(menu);
    }

    /**
     * 根据分类获取今日菜品
     */
    @GetMapping("/category/{category}")
    public Result<List<DishVO>> getTodayMenuByCategory(@PathVariable String category) {
        List<DishVO> menu = dailyMenuService.getTodayMenuByCategory(category);
        return Result.success(menu);
    }

    /**
     * 手动更新每日菜单（用于测试）
     */
    @PostMapping("/update")
    public Result<String> updateDailyMenu() {
        dailyMenuService.updateDailyMenu();
        return Result.success("每日菜单更新成功");
    }
} 