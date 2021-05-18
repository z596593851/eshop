package com.hxm.eshop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.product.entity.CategoryEntity;
import com.hxm.eshop.product.vo.Catalogs2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author xiaoming
 * @email sunlightcs@gmail.com
 * @date 2021-04-26 21:30:08
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List <CategoryEntity> listTree();

    List<Long> findCatelogPath(Long catelogId);

    List<CategoryEntity> getLevel1Categories();

    Map<String, List<Catalogs2Vo>> getCatalogJson();

    void updateCascade(CategoryEntity category);
}

