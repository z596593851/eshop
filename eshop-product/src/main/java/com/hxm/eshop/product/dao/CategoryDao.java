package com.hxm.eshop.product.dao;

import com.hxm.eshop.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author xiaoming
 * @email sunlightcs@gmail.com
 * @date 2021-04-26 21:30:08
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
