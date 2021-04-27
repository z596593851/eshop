package com.hxm.eshop.order.dao;

import com.hxm.eshop.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-04-27 16:26:19
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
