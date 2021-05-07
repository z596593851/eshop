package com.hxm.eshop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.Query;
import com.hxm.eshop.product.dao.CategoryDao;
import com.hxm.eshop.product.entity.CategoryEntity;
import com.hxm.eshop.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listTree() {
        List<CategoryEntity> categories = baseMapper.selectList(null);
        List<CategoryEntity> menus = categories.stream()
                .filter(category -> category.getParentCid() == 0)
                .peek(category -> category.setChildren(getChildren(category, categories)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
        return menus;
    }

    @Override
    public List<Long> findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        findParentPath(catelogId, paths);
        return paths;
    }

    private void findParentPath(Long catelogId,List<Long> paths){
        CategoryEntity category = this.getById(catelogId);
        if(category.getParentCid()!=0){
            findParentPath(category.getParentCid(),paths);
        }
        paths.add(catelogId);

    }

    private List<CategoryEntity> getChildren(CategoryEntity paCategory, List<CategoryEntity> categories){
        return categories.stream()
                .filter(category-> category.getParentCid().equals(paCategory.getCatId()))
                .peek(category-> category.setChildren(getChildren(category,categories)))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
    }






}