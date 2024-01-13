package com.idealzouhu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idealzouhu.reggie.common.R;
import com.idealzouhu.reggie.entity.Category;
import com.idealzouhu.reggie.entity.Employee;
import com.idealzouhu.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类（菜品分类 或者 套餐分类）
     * @param category
     * @return
     */
    @PostMapping("/addCategory")
    public R<String> save(@RequestBody Category category){
        log.info("category: {}", category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/deleCategory")
    public R<String> deleCategory(Long id){
        log.info("删除分类的id为{}", id);

        categoryService.remove(id);
        return R.success("成功删除分类信息!");
    }

    /**
     * 根据 id 修改分类信息
     * @param category
     * @return
     */
    @PutMapping("/editCategory")
    public R<String> editCategory(@RequestBody Category category){
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("成功修改分类信息 !");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        log.info("page = {}, pageSize = {}", page, pageSize);

        // 1. 构造分页查询器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        // 2. 构造条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.orderByAsc(Category::getSort);  // 添加排序条件

        // 3.执行查询
        categoryService.page(pageInfo, lambdaQueryWrapper);

        return R.success(pageInfo);
    }

}
