package com.canteen.service;

import com.canteen.vo.SetmealVO;
import java.util.List;

/**
 * 套餐服务接口
 */
public interface SetmealService {
    /**
     * 根据ID查询套餐
     * @param id 套餐ID
     * @return 套餐信息
     */
    SetmealVO getById(Long id);

    /**
     * 查询所有套餐
     * @return 套餐列表
     */
    List<SetmealVO> list();

    /**
     * 根据条件查询套餐
     * @param suitableCrowd 适合人群
     * @param vitamin 维生素类型
     * @return 套餐列表
     */
    List<SetmealVO> list(String suitableCrowd, String vitamin);

     /**
     * 自动生成四种套餐
     * @return 生成的套餐列表
     */
    List<SetmealVO> generateSetmeals();
}