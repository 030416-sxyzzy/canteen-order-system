package com.canteen.controller;

import com.canteen.common.Result;
import com.canteen.service.SetmealService;
import com.canteen.vo.SetmealVO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 套餐控制器
 */
@RestController
@RequestMapping("/api/setmeals")
public class SetmealController {

    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    /**
     * 查询所有套餐（前端统一接口）
     */
    @GetMapping
    public Result<List<SetmealVO>> list(
            @RequestParam(required = false) String suitableCrowd,
            @RequestParam(required = false) String vitamin) {
        List<SetmealVO> setmealVOList = setmealService.list(suitableCrowd, vitamin);
        return Result.success(setmealVOList);
    }

    /**
     * 自动生成套餐
     */
    @PostMapping("/generate")
    public Result<List<SetmealVO>> generateSetmeals() {
        List<SetmealVO> setmeals = setmealService.generateSetmeals();
        return Result.success(setmeals);
    }

}