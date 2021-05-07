package com.hxm.eshop.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.Query;
import org.springframework.stereotype.Service;
import java.util.Map;


import com.hxm.eshop.order.dao.OrderReturnReasonDao;
import com.hxm.eshop.order.entity.OrderReturnReasonEntity;
import com.hxm.eshop.order.service.OrderReturnReasonService;


@Service("orderReturnReasonService")
public class OrderReturnReasonServiceImpl extends ServiceImpl<OrderReturnReasonDao, OrderReturnReasonEntity> implements OrderReturnReasonService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderReturnReasonEntity> page = this.page(
                new Query<OrderReturnReasonEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

}