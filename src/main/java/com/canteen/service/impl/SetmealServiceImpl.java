package com.canteen.service.impl;
import com.canteen.entity.Dish;
import com.canteen.entity.Setmeal;
import com.canteen.entity.SetmealDish;
import com.canteen.mapper.DishMapper;
import com.canteen.mapper.SetmealDishMapper;
import com.canteen.mapper.SetmealMapper;
import com.canteen.service.SetmealService;
import com.canteen.vo.SetmealDishVO;
import com.canteen.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 套餐服务实现类
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    private final SetmealMapper setmealMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final DishMapper dishMapper;

    public SetmealServiceImpl(SetmealMapper setmealMapper, SetmealDishMapper setmealDishMapper, DishMapper dishMapper) {
        this.setmealMapper = setmealMapper;
        this.setmealDishMapper = setmealDishMapper;
        this.dishMapper = dishMapper;
    }

    @Override
    public SetmealVO getById(Long id) {
        // 1. 查询套餐基本信息
        Setmeal setmeal = setmealMapper.selectById(id);
        if (setmeal == null) {
            return null;
        }
        
        // 2. 查询套餐菜品关联
        List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(id);
        
        // 3. 转换为VO对象
        SetmealVO setmealVO = convertToVO(setmeal);
        setmealVO.setSetmealDishes(convertToDishVO(setmealDishes));
        
        return setmealVO;
    }

    @Override
    public List<SetmealVO> list() {
        // 1. 查询所有套餐
        List<Setmeal> setmeals = setmealMapper.selectAll();
        
        // 2. 转换为VO对象
        return setmeals.stream().map(setmeal -> {
            SetmealVO setmealVO = convertToVO(setmeal);
            List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(setmeal.getId());
            setmealVO.setSetmealDishes(convertToDishVO(setmealDishes));
            return setmealVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SetmealVO> list(String suitableCrowd, String vitamin) {
        // 1. 根据条件查询套餐
        List<Setmeal> setmeals = setmealMapper.selectByConditions(suitableCrowd, vitamin);
        
        // 2. 转换为VO对象
        return setmeals.stream().map(setmeal -> {
            SetmealVO setmealVO = convertToVO(setmeal);
            List<SetmealDish> setmealDishes = setmealDishMapper.selectBySetmealId(setmeal.getId());
            setmealVO.setSetmealDishes(convertToDishVO(setmealDishes));
            return setmealVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SetmealVO> generateSetmeals() {
        // 获取所有可用菜品
        List<Dish> allDishes = dishMapper.selectAvailable();
        
        // 按分类分组菜品
        Map<String, List<Dish>> dishesByCategory = allDishes.stream()
            .collect(Collectors.groupingBy(Dish::getCategory));
        
        List<SetmealVO> generatedSetmeals = new ArrayList<>();
        
        // 生成四种套餐
        generatedSetmeals.add(generateStudentSetmeal(dishesByCategory));
        generatedSetmeals.add(generateOfficeWorkerSetmeal(dishesByCategory));
        generatedSetmeals.add(generateFitnessSetmeal(dishesByCategory));
        generatedSetmeals.add(generateVegetarianSetmeal(dishesByCategory));
        
        return generatedSetmeals;
    }

    // 学生营养套餐
    private SetmealVO generateStudentSetmeal(Map<String, List<Dish>> dishesByCategory) {
        List<SetmealDishVO> selectedDishes = new ArrayList<>();
        double currentPrice = 0.0;
        Set<String> vitamins = new HashSet<>();
        
        // 简单选择：每个分类选一个菜品
        if (dishesByCategory.containsKey("主食") && !dishesByCategory.get("主食").isEmpty()) {
            Dish mainDish = dishesByCategory.get("主食").get(0);
            selectedDishes.add(createSetmealDishVO(mainDish, 1));
            currentPrice += mainDish.getPrice();
            vitamins.add("维生素B");
        }
        
        if (dishesByCategory.containsKey("荤菜") && !dishesByCategory.get("荤菜").isEmpty()) {
            Dish meatDish = dishesByCategory.get("荤菜").get(0);
            selectedDishes.add(createSetmealDishVO(meatDish, 1));
            currentPrice += meatDish.getPrice();
            vitamins.add("维生素A");
        }
        
        if (dishesByCategory.containsKey("素菜") && !dishesByCategory.get("素菜").isEmpty()) {
            Dish vegDish = dishesByCategory.get("素菜").get(0);
            selectedDishes.add(createSetmealDishVO(vegDish, 1));
            currentPrice += vegDish.getPrice();
            vitamins.add("维生素C");
        }
        
        if (dishesByCategory.containsKey("汤类") && !dishesByCategory.get("汤类").isEmpty()) {
            Dish soupDish = dishesByCategory.get("汤类").get(0);
            selectedDishes.add(createSetmealDishVO(soupDish, 1));
            currentPrice += soupDish.getPrice();
            vitamins.add("维生素D");
        }
        
        return createSetmealVO("学生营养套餐", currentPrice, "适合学生的营养均衡套餐", 
                              String.join(",", vitamins), selectedDishes);
    }

    // 上班族套餐
    private SetmealVO generateOfficeWorkerSetmeal(Map<String, List<Dish>> dishesByCategory) {
        List<SetmealDishVO> selectedDishes = new ArrayList<>();
        double currentPrice = 0.0;
        Set<String> vitamins = new HashSet<>();
        
        // 简单选择：每个分类选一个菜品
        if (dishesByCategory.containsKey("主食") && !dishesByCategory.get("主食").isEmpty()) {
            Dish mainDish = dishesByCategory.get("主食").get(0);
            selectedDishes.add(createSetmealDishVO(mainDish, 1));
            currentPrice += mainDish.getPrice();
            vitamins.add("维生素B");
        }
        
        if (dishesByCategory.containsKey("荤菜") && !dishesByCategory.get("荤菜").isEmpty()) {
            Dish meatDish = dishesByCategory.get("荤菜").get(0);
            selectedDishes.add(createSetmealDishVO(meatDish, 1));
            currentPrice += meatDish.getPrice();
            vitamins.add("维生素A");
        }
        
        if (dishesByCategory.containsKey("素菜") && !dishesByCategory.get("素菜").isEmpty()) {
            Dish vegDish = dishesByCategory.get("素菜").get(0);
            selectedDishes.add(createSetmealDishVO(vegDish, 1));
            currentPrice += vegDish.getPrice();
            vitamins.add("维生素C");
        }
        
        if (dishesByCategory.containsKey("汤类") && !dishesByCategory.get("汤类").isEmpty()) {
            Dish soupDish = dishesByCategory.get("汤类").get(0);
            selectedDishes.add(createSetmealDishVO(soupDish, 1));
            currentPrice += soupDish.getPrice();
            vitamins.add("维生素D");
        }
        
        return createSetmealVO("上班族套餐", currentPrice, "适合上班族的营养丰富套餐", 
                              String.join(",", vitamins), selectedDishes);
    }

    // 健身套餐
    private SetmealVO generateFitnessSetmeal(Map<String, List<Dish>> dishesByCategory) {
        List<SetmealDishVO> selectedDishes = new ArrayList<>();
        double currentPrice = 0.0;
        Set<String> vitamins = new HashSet<>();
        
        // 简单选择：每个分类选一个菜品
        if (dishesByCategory.containsKey("主食") && !dishesByCategory.get("主食").isEmpty()) {
            Dish mainDish = dishesByCategory.get("主食").get(0);
            selectedDishes.add(createSetmealDishVO(mainDish, 1));
            currentPrice += mainDish.getPrice();
            vitamins.add("维生素B");
        }
        
        if (dishesByCategory.containsKey("荤菜") && !dishesByCategory.get("荤菜").isEmpty()) {
            Dish meatDish = dishesByCategory.get("荤菜").get(0);
            selectedDishes.add(createSetmealDishVO(meatDish, 1));
            currentPrice += meatDish.getPrice();
            vitamins.add("维生素A");
        }
        
        if (dishesByCategory.containsKey("素菜") && !dishesByCategory.get("素菜").isEmpty()) {
            Dish vegDish = dishesByCategory.get("素菜").get(0);
            selectedDishes.add(createSetmealDishVO(vegDish, 1));
            currentPrice += vegDish.getPrice();
            vitamins.add("维生素C");
        }
        
        if (dishesByCategory.containsKey("汤类") && !dishesByCategory.get("汤类").isEmpty()) {
            Dish soupDish = dishesByCategory.get("汤类").get(0);
            selectedDishes.add(createSetmealDishVO(soupDish, 1));
            currentPrice += soupDish.getPrice();
            vitamins.add("维生素D");
        }
        
        return createSetmealVO("健身套餐", currentPrice, "适合健身人士的高蛋白套餐", 
                              String.join(",", vitamins), selectedDishes);
    }

    // 素食者套餐
    private SetmealVO generateVegetarianSetmeal(Map<String, List<Dish>> dishesByCategory) {
        List<SetmealDishVO> selectedDishes = new ArrayList<>();
        double currentPrice = 0.0;
        Set<String> vitamins = new HashSet<>();
        
        // 简单选择：每个分类选一个菜品（不选荤菜）
        if (dishesByCategory.containsKey("主食") && !dishesByCategory.get("主食").isEmpty()) {
            Dish mainDish = dishesByCategory.get("主食").get(0);
            selectedDishes.add(createSetmealDishVO(mainDish, 1));
            currentPrice += mainDish.getPrice();
            vitamins.add("维生素B");
        }
        
        if (dishesByCategory.containsKey("素菜") && !dishesByCategory.get("素菜").isEmpty()) {
            Dish vegDish = dishesByCategory.get("素菜").get(0);
            selectedDishes.add(createSetmealDishVO(vegDish, 1));
            currentPrice += vegDish.getPrice();
            vitamins.add("维生素C");
        }
        
        if (dishesByCategory.containsKey("汤类") && !dishesByCategory.get("汤类").isEmpty()) {
            Dish soupDish = dishesByCategory.get("汤类").get(0);
            selectedDishes.add(createSetmealDishVO(soupDish, 1));
            currentPrice += soupDish.getPrice();
            vitamins.add("维生素D");
        }
        
        return createSetmealVO("素食者套餐", currentPrice, "适合素食主义者的全素营养套餐", 
                              String.join(",", vitamins), selectedDishes);
    }

    // 辅助方法
    private SetmealDishVO createSetmealDishVO(Dish dish, int copies) {
        SetmealDishVO vo = new SetmealDishVO();
        vo.setDishId(dish.getId());
        vo.setDishName(dish.getName());
        vo.setDishPrice(dish.getPrice());
        vo.setCopies(copies);
        return vo;
    }

    private SetmealVO createSetmealVO(String name, double price, String description, 
                                     String vitaminType, List<SetmealDishVO> dishes) {
        SetmealVO setmealVO = new SetmealVO();
        setmealVO.setId(System.currentTimeMillis()); // 临时ID
        setmealVO.setName(name);
        setmealVO.setPrice(BigDecimal.valueOf(price));
        setmealVO.setDescription(description);
        setmealVO.setVitaminType(vitaminType);
        setmealVO.setStatus(1);
        setmealVO.setStatusDesc("起售");
        setmealVO.setSetmealDishes(dishes);
        return setmealVO;
    }

    /**
     * 将Setmeal对象转换为SetmealVO对象
     */
    private SetmealVO convertToVO(Setmeal setmeal) {
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setStatusDesc(setmeal.getStatus() == 1 ? "起售" : "停售");
        return setmealVO;
    }

    /**
     * 将SetmealDish对象列表转换为SetmealDishVO对象列表
     */
    private List<SetmealDishVO> convertToDishVO(List<SetmealDish> setmealDishes) {
        return setmealDishes.stream().map(setmealDish -> {
            SetmealDishVO dishVO = new SetmealDishVO();
            dishVO.setDishId(setmealDish.getDishId());
            dishVO.setDishName(setmealDish.getDishName());
            dishVO.setDishPrice(setmealDish.getDishPrice());
            dishVO.setCopies(setmealDish.getCopies());
            return dishVO;
        }).collect(Collectors.toList());
    }
}