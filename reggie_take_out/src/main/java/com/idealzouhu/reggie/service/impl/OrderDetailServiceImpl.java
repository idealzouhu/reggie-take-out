package com.idealzouhu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idealzouhu.reggie.entity.OrderDetail;
import com.idealzouhu.reggie.mapper.OrderDetailMapper;
import com.idealzouhu.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
