package com.hxm.eshop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hxm.eshop.product.dao.BrandDao;
import com.hxm.eshop.product.dao.CategoryDao;
import com.hxm.eshop.product.entity.BrandEntity;
import com.hxm.eshop.product.entity.CategoryEntity;
import com.hxm.eshop.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.Query;

import com.hxm.eshop.product.dao.CategoryBrandRelationDao;
import com.hxm.eshop.product.entity.CategoryBrandRelationEntity;
import com.hxm.eshop.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryBrandRelationDao relationDao;
    @Autowired
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        //查询详细名字
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);
        this.update(relationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {

        List<CategoryBrandRelationEntity> categoryBrandRelations = relationDao.selectList(
                new QueryWrapper<CategoryBrandRelationEntity>()
                        .eq("catelog_id", catId));
        return categoryBrandRelations.stream()
                .map(item -> {
                    Long brandId = item.getBrandId();
                    return brandService.getById(brandId); })
                .collect(Collectors.toList());
    }

    @Override
    public void updateCategory(Long catId, String name) {
        updateCategory(catId,name);
    }

}