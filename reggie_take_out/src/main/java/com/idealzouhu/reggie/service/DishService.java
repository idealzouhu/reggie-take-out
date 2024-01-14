package com.idealzouhu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idealzouhu.reggie.dto.DishDto;
import com.idealzouhu.reggie.entity.Dish;
import org.springframework.stereotype.Service;

@Service
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品，同时插入菜品对应的口味数据，需要操作两张表： dish 、dish_flavor
     * @Param dishdto
     */
    public void saveWithFlavor(DishDto dishdto);

    /**
     * 根据 id 查询菜品和菜品口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息，同时菜品口味信息
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);
}
