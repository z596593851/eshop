package com.hxm.eshop.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.to.SkuHasStockVo;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-12 16:08:56
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

