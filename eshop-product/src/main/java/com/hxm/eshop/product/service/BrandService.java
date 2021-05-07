package com.hxm.eshop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author xiaoming
 * @email sunlightcs@gmail.com
 * @date 2021-04-26 21:30:08
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

