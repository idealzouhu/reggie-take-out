package com.idealzouhu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idealzouhu.reggie.entity.ShoppingCart;
import com.idealzouhu.reggie.mapper.ShoppingCartMapper;
import com.idealzouhu.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
