package com.canteen.controller;

import com.canteen.common.Result;
import com.canteen.service.UserPreferenceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户偏好分析控制器
 */
@RestController
@RequestMapping("/api/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    /**
     * 获取菜品偏好分析
     */
    @GetMapping("/dishes")
    public Result<List<Map<String, Object>>> getDishAnalysis() {
        // 使用真实数据而不是模拟数据
        List<Map<String, Object>> dishStats = userPreferenceService.getDishPurchaseStats();
        return Result.success(dishStats);
    }

    /**
     * 获取菜品偏好统计
     */
    @GetMapping("/dish-ranking")
    public Result<List<Map<String, Object>>> getDishRanking() {
        List<Map<String, Object>> dishStats = userPreferenceService.getDishPurchaseStats();
        return Result.success(dishStats);
    }

    /**
     * 获取营养偏好分析
     */
    @GetMapping("/nutrition-analysis")
    public Result<List<Map<String, Object>>> getNutritionAnalysis() {
        List<Map<String, Object>> nutritionStats = userPreferenceService.getNutritionPreferenceStats();
        return Result.success(nutritionStats);
    }

    /**
     * 获取菜品喜爱表
     */
    @GetMapping("/dish-likes-table")
    public Result<List<Map<String, Object>>> getDishLikesTable() {
        List<Map<String, Object>> dishTable = userPreferenceService.generateDishPreferenceTable();
        return Result.success(dishTable);
    }

    /**
     * 生成菜品喜爱表
     */
    @PostMapping("/generate-dish-likes-table")
    public Result<Void> generateDishLikesTable() {
        // 这里可以添加生成逻辑，目前直接返回成功
        return Result.success();
    }

    /**
     * 导出分析报告
     */
    @GetMapping("/export")
    public Result<Void> exportAnalysis() {
        // 这里可以添加导出逻辑，目前直接返回成功
        return Result.success();
    }

    /**
     * 根据营养人群获取菜品偏好分析
     */
    @GetMapping("/dishes-by-crowd/{crowdType}")
    public Result<List<Map<String, Object>>> getDishAnalysisByCrowd(@PathVariable String crowdType) {
        List<Map<String, Object>> dishStats = userPreferenceService.getDishPurchaseStatsByCrowd(crowdType);
        return Result.success(dishStats);
    }

    /**
     * 获取套餐购买量统计
     */
    @GetMapping("/setmeal-purchase-stats")
    public Result<List<Map<String, Object>>> getSetmealPurchaseStats() {
        List<Map<String, Object>> setmealStats = userPreferenceService.getSetmealPurchaseStats();
        return Result.success(setmealStats);
    }

    /**
     * 生成食堂交互建议
     */
    @GetMapping("/canteen-suggestions")
    public Result<List<Map<String, Object>>> getCanteenSuggestions() {
        List<Map<String, Object>> suggestions = userPreferenceService.generateCanteenSuggestions();
        return Result.success(suggestions);
    }
} 