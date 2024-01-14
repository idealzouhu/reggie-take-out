package com.idealzouhu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idealzouhu.reggie.common.CustomException;
import com.idealzouhu.reggie.dto.SetmealDto;
import com.idealzouhu.reggie.entity.DishFlavor;
import com.idealzouhu.reggie.entity.Setmeal;
import com.idealzouhu.reggie.entity.SetmealDish;
import com.idealzouhu.reggie.mapper.SetmealMapper;
import com.idealzouhu.reggie.service.SetmealDishService;
import com.idealzouhu.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，并保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void savewithDish(SetmealDto setmealDto) {
        // 保存套餐信息
        this.save(setmealDto);  // 为什么这个实体还包括了其他信息，却不会报错

        // 保存套餐和菜品的关联信息， 操作 set_meal表，执行 insert 操作
        Long setmealDtoId = setmealDto.getId();
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList.stream().map((item)->{
            item.setSetmealId(setmealDtoId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishList);

    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，查看是否删除。如果不能删除，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);
        if(count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        // 如果可以删除，先删除 setmeal表 中的数据
        this.removeByIds(ids);

        // 然后删除 setmeal_dish表 中的数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

    }
}
