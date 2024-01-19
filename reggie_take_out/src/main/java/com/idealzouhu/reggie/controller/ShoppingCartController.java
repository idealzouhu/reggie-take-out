package com.idealzouhu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.idealzouhu.reggie.common.BaseContext;
import com.idealzouhu.reggie.common.R;
import com.idealzouhu.reggie.entity.Setmeal;
import com.idealzouhu.reggie.entity.ShoppingCart;
import com.idealzouhu.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车功能实现
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());

        // 设置用户 id，指定当前是哪个用户添加的数据
        Long currrentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currrentId);

        // 查询当前菜品或者套餐是否在购物车中。
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if(dishId != null){
            // 添加到购物车的是菜品
            lambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else{
            // 添加到购物车的是套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // SQL: select * from shopping_cart where user_id = ？ and dish_id/setmeal_id = ?
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lambdaQueryWrapper);

        if(shoppingCart1 != null){
            // 如果某个菜品或者套餐已经存在，实际上修改数据即可
            shoppingCart1.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartService.updateById(shoppingCart1);
        }else {
            // 如果不存在，则添加到购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }

        return R.success(shoppingCart1);
    }

    /**
     * 清空购物表
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        // SQL: delete from where shopping_cart user_id = ?
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lambdaQueryWrapper);
        return R.success("成功清空购物车！");
    }

    /**
     * 给出购物车列表信息
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);

        return R.success(list);

    }
}
