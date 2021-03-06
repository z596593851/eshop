package com.hxm.eshop.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.to.SkuHasStockVo;
import com.hxm.common.to.mq.OrderTo;
import com.hxm.common.to.mq.StockLockedTo;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.ware.entity.WareSkuEntity;
import com.hxm.eshop.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * εεεΊε­
 *
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-12 16:08:56
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo to);
}

