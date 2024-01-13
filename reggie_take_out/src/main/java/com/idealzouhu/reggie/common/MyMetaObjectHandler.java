package com.idealzouhu.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // log.info("公共字段自动填充[insert]...");
        // log.info("线程id: {}" , Thread.currentThread().getId()) ;
        metaObject.setValue("createTime", LocalDateTime.now());  // createTime 为 Employee 实体里面的属性
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // log.info("公共字段自动填充[update]...");
        // log.info(metaObject.toString());
        // log.info("线程id: {}" , Thread.currentThread().getId()) ;
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
