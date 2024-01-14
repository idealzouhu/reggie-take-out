package com.idealzouhu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idealzouhu.reggie.dto.SetmealDto;
import com.idealzouhu.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，并保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void savewithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
