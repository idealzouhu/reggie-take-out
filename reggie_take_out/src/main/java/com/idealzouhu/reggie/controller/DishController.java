package com.idealzouhu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idealzouhu.reggie.common.R;
import com.idealzouhu.reggie.dto.DishDto;
import com.idealzouhu.reggie.entity.Category;
import com.idealzouhu.reggie.entity.Dish;
import com.idealzouhu.reggie.entity.Employee;
import com.idealzouhu.reggie.service.CategoryService;
import com.idealzouhu.reggie.service.DishFlavorService;
import com.idealzouhu.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping("/addDish")
    public R<String> addDish(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping("/editDish")
    public R<String> editDish(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        // 根据菜品分类 CategoryId 查询符合的菜品数据集合
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus, 1);  // 只显示启售状态的菜品
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lambdaQueryWrapper);

        return R.success(list);
    }

    /**
     * 根据 id 查看菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> queryDishById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 分页查询菜品信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name={} ", page, pageSize,name);

        // 1. 构造分页查询器
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();


        // 2. 构造条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name); // 使用模糊查询
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);  // 添加排序条件(根据时间降序排列)

        // 3.执行分析查询
        dishService.page(pageInfo, lambdaQueryWrapper);

        // 4. 对象拷贝（查询信息除了 Dish 对象还包括其他信息）
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");  // 复制除了 records 以外的所有信息

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map((item)->{     // 将records信息和分类名组合成新的records
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long catogoryId = item.getCategoryId(); // 分类 id
            Category category = categoryService.getById(catogoryId);
            if(category != null){
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }

}
