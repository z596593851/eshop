package com.hxm.eshop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author xiaoming
 * @email sunlightcs@gmail.com
 * @date 2021-04-26 21:30:08
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfoDesc(SpuInfoDescEntity descEntity);
}

