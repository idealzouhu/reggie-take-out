> 本文档主要实现分类管理业务，包括 公告字段自动填充，新增分类，分类信息分页查询，删除分类，修改分类



# 一、公共字段自动填充

## 1.1 问题分析

前面我们已经完成了员工信息管理。但是，我们在新增员工时需要设置创建时间、创建人、修改时间、修改人等字段，在编辑员工时需要设置修改时间和修改人等字段。这些字段属于公共字段。

能不能对于这些公共字段在某个地方统一处理，来简化开发？我们可以使用 Mybatis-Plus 提供的<font color="red">**公共字段自动填充**</font>功能

Mybatis Plus公共字段自动填充，也就是在插入或者更新的时候为指定字段赋予指定的值，使用它的好处就是可以统一对这些字段进行处理，避免了重复代码。

## 1.2 代码实现

公共字段自动填充实现步骤:

1. 在实体类的属性上加入@TableField注解，指定自动填充的策略。对于 `src/main/java/com/idealzouhu/reggie/entity/Employee.java`， 更新代码

   ```
   @TableField(fill = FieldFill.INSERT)  // 插入时填充字段
   private LocalDateTime createTime;
   
   @TableField(fill = FieldFill.INSERT_UPDATE) //  插入和更新时填充字段
   private LocalDateTime updateTime;
   
   @TableField(fill = FieldFill.INSERT)
   private Long createUser;
   
   @TableField(fill = FieldFill.INSERT_UPDATE)
   private Long updateUser;
   ```

2. 按照框架要求编写**元数据对象处理器**，在此类中统一为公共字段赋值，此类需要实现 MetaObjectHandler 接口。





## 1.3 功能测试



## 1.4 功能完善