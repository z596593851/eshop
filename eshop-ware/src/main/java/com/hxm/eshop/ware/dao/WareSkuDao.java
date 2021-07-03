package com.hxm.eshop.ware.dao;

import com.hxm.eshop.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-12 16:08:56
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Long getSkuStock(Long item);

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);

    /**
     * 解锁库存
     * @param skuId
     * @param wareId
     * @param num
     */
    void unLockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);

    List<Long> listWareIdHasSkuStock(Long skuId);
}
