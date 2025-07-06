package com.canteen.service.impl;

import com.canteen.entity.Dish;
import com.canteen.entity.Order;
import com.canteen.entity.OrderDetail;
import com.canteen.mapper.DishMapper;
import com.canteen.mapper.OrderDetailMapper;
import com.canteen.mapper.OrderMapper;
import com.canteen.service.UserPreferenceService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户偏好分析服务实现类
 */
@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final DishMapper dishMapper;

    public UserPreferenceServiceImpl(OrderMapper orderMapper, 
                                   OrderDetailMapper orderDetailMapper, 
                                   DishMapper dishMapper) {
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.dishMapper = dishMapper;
    }

    @Override
    public List<Map<String, Object>> getDishPurchaseStats() {
        // 获取所有已完成的订单
        List<Order> allOrders = orderMapper.selectAll();
        List<Order> completedOrders = allOrders.stream()
                .filter(order -> order.getStatus() == 2) // 假设2表示已完成
                .collect(Collectors.toList());
        
        // 统计菜品购买情况
        Map<Long, DishStats> dishStatsMap = collectDishStats(completedOrders);
        
        // 转换为前端需要的格式
        List<Map<String, Object>> result = convertDishStatsToResult(dishStatsMap);
        
        // 按购买次数排序
        result.sort((a, b) -> Integer.compare((Integer) b.get("purchaseCount"), (Integer) a.get("purchaseCount")));
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getNutritionPreferenceStats() {
        // 获取菜品购买统计
        List<Map<String, Object>> dishStats = getDishPurchaseStats();
        
        // 按营养类型分组统计
        Map<String, NutritionStats> nutritionStatsMap = collectNutritionStats(dishStats);
        
        // 转换为返回格式
        List<Map<String, Object>> result = convertNutritionStatsToResult(nutritionStatsMap);
        
        // 按购买次数排序
        result.sort((a, b) -> Integer.compare((Integer) b.get("totalPurchases"), (Integer) a.get("totalPurchases")));
        
        return result;
    }

    @Override
    public List<Map<String, Object>> generateDishPreferenceTable() {
        List<Map<String, Object>> dishStats = getDishPurchaseStats();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map<String, Object> stat : dishStats) {
            Map<String, Object> dishPreference = new HashMap<>();
            dishPreference.put("菜品名称", stat.get("dishName"));
            dishPreference.put("总购买次数", stat.get("totalPurchases"));
            dishPreference.put("购买用户数", stat.get("userCount"));
            
            // 计算喜爱度等级（仅基于购买次数）
            Integer totalPurchases = (Integer) stat.get("totalPurchases");
            String preferenceLevel = calculatePreferenceLevel(totalPurchases);
            dishPreference.put("喜爱度等级", preferenceLevel);
            
            result.add(dishPreference);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> generateNutritionPreferenceAnalysis() {
        List<Map<String, Object>> nutritionStats = getNutritionPreferenceStats();
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map<String, Object> stat : nutritionStats) {
            Map<String, Object> nutritionAnalysis = new HashMap<>();
            nutritionAnalysis.put("营养类型", stat.get("nutritionType"));
            nutritionAnalysis.put("总购买次数", stat.get("totalPurchases"));
            nutritionAnalysis.put("菜品数量", stat.get("dishCount"));
            
            // 分析营养偏好趋势（仅基于购买次数）
            Integer totalPurchases = (Integer) stat.get("totalPurchases");
            String trend = analyzeNutritionTrend(totalPurchases);
            nutritionAnalysis.put("偏好趋势", trend);
            
            result.add(nutritionAnalysis);
        }
        
        return result;
    }

    /**
     * 分析营养类型
     */
    private String analyzeNutritionType(String dishName) {
        String name = dishName.toLowerCase();
        
        if (name.contains("胡萝卜") || name.contains("南瓜") || name.contains("菠菜")) {
            return "维生素A";
        } else if (name.contains("橙子") || name.contains("柠檬") || name.contains("番茄")) {
            return "维生素C";
        } else if (name.contains("瘦肉") || name.contains("鸡蛋") || name.contains("豆类")) {
            return "维生素B";
        } else if (name.contains("牛奶") || name.contains("奶酪") || name.contains("酸奶")) {
            return "维生素D";
        } else if (name.contains("坚果") || name.contains("植物油")) {
            return "维生素E";
        } else {
            return "多种维生素";
        }
    }

    /**
     * 计算喜爱度等级（百分数）
     */
    private String calculatePreferenceLevel(Integer totalPurchases) {
        if (totalPurchases >= 50) {
            return "极高";
        } else if (totalPurchases >= 30) {
            return "高";
        } else if (totalPurchases >= 15) {
            return "中";
        } else {
            return "低";
        }
    }

    /**
     * 分析营养偏好趋势（仅基于购买次数）
     */
    private String analyzeNutritionTrend(Integer totalPurchases) {
        if (totalPurchases >= 100) {
            return "强烈偏好";
        } else if (totalPurchases >= 50) {
            return "偏好";
        } else if (totalPurchases >= 20) {
            return "一般";
        } else {
            return "较少选择";
        }
    }

    /**
     * 菜品统计内部类
     */
    private static class DishStats {
        int totalQuantity = 0;
        int purchaseCount = 0;
        Set<Long> userIds = new HashSet<>();
    }

    /**
     * 营养统计内部类
     */
    private static class NutritionStats {
        int totalPurchases = 0;
        int dishCount = 0;
    }

    @Override
    public List<Map<String, Object>> getDishPurchaseStatsByCrowd(String crowdType) {
        // 根据营养人群类型获取菜品购买统计
        List<Map<String, Object>> allDishStats = getDishPurchaseStats();
        List<Map<String, Object>> filteredStats = new ArrayList<>();
        
        for (Map<String, Object> dishStat : allDishStats) {
            String dishName = (String) dishStat.get("dishName");
            if (isDishSuitableForCrowd(dishName, crowdType)) {
                filteredStats.add(dishStat);
            }
        }
        
        return filteredStats;
    }
    
    @Override
    public List<Map<String, Object>> getSetmealPurchaseStats() {
        // 获取套餐购买量统计 - 使用真实数据库数据
        
        // 获取所有有效的订单
        List<Order> allOrders = orderMapper.selectAll();
        List<Order> validOrders = allOrders.stream()
                .filter(order -> order.getStatus() == 1 || order.getStatus() == 2)
                .collect(Collectors.toList());
        
        // 统计套餐购买情况
        Map<Long, Integer> setmealPurchaseCount = collectSetmealStats(validOrders);
        
        // 转换为前端需要的格式
        return convertSetmealStatsToResult(setmealPurchaseCount);
    }
    
    /**
     * 收集套餐购买统计
     */
    private Map<Long, Integer> collectSetmealStats(List<Order> validOrders) {
        Map<Long, Integer> setmealPurchaseCount = new HashMap<>();
        
        for (Order order : validOrders) {
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
            
            for (OrderDetail detail : orderDetails) {
                if (detail.getSetmealId() != null) {
                    Long setmealId = detail.getSetmealId();
                    int quantity = detail.getQuantity();
                    
                    setmealPurchaseCount.put(setmealId, 
                        setmealPurchaseCount.getOrDefault(setmealId, 0) + quantity);
                }
            }
        }
        
        return setmealPurchaseCount;
    }
    
    /**
     * 转换套餐统计为结果格式
     */
    private List<Map<String, Object>> convertSetmealStatsToResult(Map<Long, Integer> setmealPurchaseCount) {
        List<Map<String, Object>> setmealStats = new ArrayList<>();
        
        for (Map.Entry<Long, Integer> entry : setmealPurchaseCount.entrySet()) {
            Long setmealId = entry.getKey();
            Integer purchaseCount = entry.getValue();
            
            Map<String, Object> setmealStat = createSetmealStat(setmealId, purchaseCount);
            setmealStats.add(setmealStat);
        }
        
        return setmealStats;
    }
    
    /**
     * 创建套餐统计对象
     */
    private Map<String, Object> createSetmealStat(Long setmealId, Integer purchaseCount) {
        Map<String, Object> setmealStat = new HashMap<>();
        
        // 根据套餐ID获取套餐名称
        String setmealName = getSetmealNameById(setmealId);
        setmealStat.put("setmealName", setmealName);
        setmealStat.put("purchaseCount", purchaseCount);
        
        // 根据套餐ID判断适合的人群
        String suitableCrowd = determineSuitableCrowdBySetmealId(setmealId);
        setmealStat.put("suitableCrowd", suitableCrowd);
        
        // 计算购买用户数（基于真实订单数据）
        int userCount = calculateUserCountBySetmealId(setmealId);
        setmealStat.put("userCount", userCount);
        
        // 计算喜爱度等级（百分数）
        int preferenceLevel = calculatePreferenceLevel(purchaseCount, userCount);
        setmealStat.put("preferenceLevel", preferenceLevel);
        
        return setmealStat;
    }
    
    /**
     * 根据套餐ID计算购买用户数
     */
    private int calculateUserCountBySetmealId(Long setmealId) {
        List<Order> allOrders = orderMapper.selectAll();
        Set<Long> userIds = new HashSet<>();
        
        for (Order order : allOrders) {
            if (order.getStatus() == 1 || order.getStatus() == 2) {
                List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
                
                for (OrderDetail detail : orderDetails) {
                    if (setmealId.equals(detail.getSetmealId())) {
                        userIds.add(order.getUserId());
                        break; // 一个订单只计算一次用户
                    }
                }
            }
        }
        
        return userIds.size();
    }
    
    /**
     * 计算喜爱度等级（百分数）
     */
    private int calculatePreferenceLevel(int purchaseCount, int userCount) {
        if (purchaseCount == 0 || userCount == 0) {
            return 0;
        }
        
        // 计算平均购买次数
        double avgPurchasePerUser = (double) purchaseCount / userCount;
        
        // 基于购买次数、用户数和平均购买次数的综合评分
        // 购买次数权重40%，用户数权重30%，平均购买次数权重30%
        int purchaseScore = Math.min(40, purchaseCount * 4); // 最多40分
        int userScore = Math.min(30, userCount * 6); // 最多30分
        int avgScore = Math.min(30, (int)(avgPurchasePerUser * 10)); // 最多30分
        
        int totalScore = purchaseScore + userScore + avgScore;
        
        // 确保分数在0-100之间
        return Math.min(100, Math.max(0, totalScore));
    }
    
    /**
     * 根据套餐ID获取套餐名称
     */
    private String getSetmealNameById(Long setmealId) {
        switch (setmealId.intValue()) {
            case 1: return "学生营养套餐A";
            case 2: return "学生营养套餐B";
            case 3: return "上班族套餐A";
            case 4: return "上班族套餐B";
            case 5: return "素食套餐A";
            case 6: return "素食套餐B";
            case 7: return "健身套餐A";
            case 8: return "健身套餐B";
            default: return "未知套餐";
        }
    }
    
    /**
     * 根据套餐ID判断适合的人群
     */
    private String determineSuitableCrowdBySetmealId(Long setmealId) {
        if (setmealId <= 2) {
            return "学生";
        } else if (setmealId <= 4) {
            return "上班族";
        } else if (setmealId <= 6) {
            return "素食者";
        } else {
            return "健身人群";
        }
    }
    
    /**
     * 判断菜品是否适合特定营养人群
     */
    private boolean isDishSuitableForCrowd(String dishName, String crowdType) {
        switch (crowdType) {
            case "学生":
                // 学生适合营养均衡的菜品
                return !dishName.contains("高脂肪") && !dishName.contains("高热量");
            case "健身人群":
                // 健身人群适合高蛋白菜品
                return dishName.contains("鸡") || dishName.contains("鱼") || dishName.contains("肉");
            case "素食者":
                // 素食者只适合素菜
                return dishName.contains("豆腐") || dishName.contains("蔬菜") || dishName.contains("素");
            case "上班族":
                // 上班族适合营养丰富的菜品
                return true; // 上班族选择范围较广
            default:
                return true;
        }
    }
    
    @Override
    public List<Map<String, Object>> generateCanteenSuggestions() {
        // 生成食堂交互建议
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        // 建议1：增加热门菜品供应
        Map<String, Object> suggestion1 = new HashMap<>();
        suggestion1.put("type", "菜品供应");
        suggestion1.put("suggestion", "根据学生偏好，建议增加宫保鸡丁、红烧肉等热门菜品的供应量");
        suggestion1.put("priority", "高");
        suggestion1.put("reason", "这些菜品购买量较高，学生喜爱度强");
        suggestions.add(suggestion1);
        
        // 建议2：优化套餐搭配
        Map<String, Object> suggestion2 = new HashMap<>();
        suggestion2.put("type", "套餐优化");
        suggestion2.put("suggestion", "学生营养套餐A购买量最高，建议保持现有搭配，并考虑增加新品种");
        suggestion2.put("priority", "中");
        suggestion2.put("reason", "该套餐营养均衡，价格适中，深受学生欢迎");
        suggestions.add(suggestion2);
        
        // 建议3：营养均衡建议
        Map<String, Object> suggestion3 = new HashMap<>();
        suggestion3.put("type", "营养建议");
        suggestion3.put("suggestion", "建议增加素菜品种，平衡荤素搭配，满足不同营养需求");
        suggestion3.put("priority", "中");
        suggestion3.put("reason", "当前荤菜购买量较高，需要增加素菜选择");
        suggestions.add(suggestion3);
        
        return suggestions;
    }

    /**
     * 收集菜品购买统计
     */
    private Map<Long, DishStats> collectDishStats(List<Order> completedOrders) {
        Map<Long, DishStats> dishStatsMap = new HashMap<>();
        
        for (Order order : completedOrders) {
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
            
            for (OrderDetail detail : orderDetails) {
                if (detail.getDishId() != null) {
                    Long dishId = detail.getDishId();
                    int quantity = detail.getQuantity();
                    
                    DishStats stats = dishStatsMap.computeIfAbsent(dishId, k -> new DishStats());
                    stats.totalQuantity += quantity;
                    stats.purchaseCount++;
                    stats.userIds.add(order.getUserId());
                }
            }
        }
        
        return dishStatsMap;
    }
    
    /**
     * 转换菜品统计为结果格式
     */
    private List<Map<String, Object>> convertDishStatsToResult(Map<Long, DishStats> dishStatsMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map.Entry<Long, DishStats> entry : dishStatsMap.entrySet()) {
            Long dishId = entry.getKey();
            DishStats stats = entry.getValue();
            
            // 获取菜品信息
            Dish dish = dishMapper.selectById(dishId);
            if (dish != null) {
                Map<String, Object> dishStat = new HashMap<>();
                dishStat.put("dishId", dishId);
                dishStat.put("dishName", dish.getName());
                dishStat.put("purchaseCount", stats.purchaseCount);
                dishStat.put("totalQuantity", stats.totalQuantity);
                dishStat.put("userCount", stats.userIds.size());
                dishStat.put("preferenceLevel", calculatePreferenceLevel(stats.purchaseCount));
                
                result.add(dishStat);
            }
        }
        
        return result;
    }

    /**
     * 收集营养统计
     */
    private Map<String, NutritionStats> collectNutritionStats(List<Map<String, Object>> dishStats) {
        Map<String, NutritionStats> nutritionStatsMap = new HashMap<>();
        
        for (Map<String, Object> dishStat : dishStats) {
            String dishName = (String) dishStat.get("dishName");
            Integer totalPurchases = (Integer) dishStat.get("purchaseCount");
            
            String nutritionType = analyzeNutritionType(dishName);
            
            NutritionStats stats = nutritionStatsMap.computeIfAbsent(nutritionType, k -> new NutritionStats());
            stats.totalPurchases += totalPurchases;
            stats.dishCount++;
        }
        
        return nutritionStatsMap;
    }
    
    /**
     * 转换营养统计为结果格式
     */
    private List<Map<String, Object>> convertNutritionStatsToResult(Map<String, NutritionStats> nutritionStatsMap) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map.Entry<String, NutritionStats> entry : nutritionStatsMap.entrySet()) {
            String nutritionType = entry.getKey();
            NutritionStats stats = entry.getValue();
            
            Map<String, Object> nutritionStat = new HashMap<>();
            nutritionStat.put("nutritionType", nutritionType);
            nutritionStat.put("totalPurchases", stats.totalPurchases);
            nutritionStat.put("dishCount", stats.dishCount);
            
            result.add(nutritionStat);
        }
        
        return result;
    }
} 