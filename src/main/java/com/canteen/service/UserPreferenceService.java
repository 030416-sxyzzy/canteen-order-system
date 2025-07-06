package com.canteen.service;

import java.util.List;
import java.util.Map;

/**
 * 用户偏好分析服务接口
 */
public interface UserPreferenceService {
    
    /**
     * 获取菜品购买统计
     */
    List<Map<String, Object>> getDishPurchaseStats();
    
    /**
     * 获取用户营养偏好统计
     */
    List<Map<String, Object>> getNutritionPreferenceStats();
    
    /**
     * 生成菜品喜爱表
     */
    List<Map<String, Object>> generateDishPreferenceTable();
    
    /**
     * 生成营养偏好分析
     */
    List<Map<String, Object>> generateNutritionPreferenceAnalysis();
    
    /**
     * 根据营养人群获取菜品购买统计
     */
    List<Map<String, Object>> getDishPurchaseStatsByCrowd(String crowdType);
    
    /**
     * 获取套餐购买量统计
     */
    List<Map<String, Object>> getSetmealPurchaseStats();
    
    /**
     * 生成食堂交互建议
     */
    List<Map<String, Object>> generateCanteenSuggestions();
} 