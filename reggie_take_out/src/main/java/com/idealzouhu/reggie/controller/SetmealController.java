package com.idealzouhu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idealzouhu.reggie.common.R;
import com.idealzouhu.reggie.dto.DishDto;
import com.idealzouhu.reggie.dto.SetmealDto;
import com.idealzouhu.reggie.entity.Category;
import com.idealzouhu.reggie.entity.Dish;
import com.idealzouhu.reggie.entity.Setmeal;
import com.idealzouhu.reggie.service.CategoryService;
import com.idealzouhu.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐， 并向 setmeal_dish表 里面新增数据
     */
    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.savewithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 删除套餐 （单个 或者 批量）， 还要删除套餐与菜品的关联数据
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("成功删除套餐");
    }

    /**
     * 分页查询套餐信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        // 1. 分页构造器对象
        Page<Setmeal> pageInfo = new Page<>();
        Page<SetmealDto> setmealDtoPage = new Page<>();

        // 2. 条件构造器
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        // 3. 执行分析查询
        setmealService.page(pageInfo, lambdaQueryWrapper);

        // 4. 对象拷贝（查询信息除了 Setmeal 对象还包括其他信息）, 需要重新组合 records
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");  // 复制除了 records 以外的所有信息
        List<Setmeal> setmealList = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = setmealList.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(categoryId != null){
                setmealDto.setCategoryName(category.getName());
            }
            return  setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(setmealDtoList);

        return R.success(setmealDtoPage);
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);

        return R.success(list);

    }
}
