package com.idealzouhu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idealzouhu.reggie.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends IService<Category> {
    /**
     * 自定义的 remove 方法
     * @param id
     * @return
     */
    boolean remove(Long id);
}
