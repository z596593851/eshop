package com.hxm.eshop.coupon.dao;

import com.hxm.eshop.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-08 14:48:37
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
