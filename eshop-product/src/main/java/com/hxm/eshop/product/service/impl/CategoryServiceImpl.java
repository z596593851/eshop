package com.hxm.eshop.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.Query;
import com.hxm.eshop.product.dao.CategoryDao;
import com.hxm.eshop.product.entity.CategoryEntity;
import com.hxm.eshop.product.service.CategoryBrandRelationService;
import com.hxm.eshop.product.service.CategoryService;
import com.hxm.eshop.product.vo.Catalogs2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
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

    /**
     * ??????????????????????????????????????????db????????????
     * @CacheEvict(value = "category", key="'getLevel1Categories'")?????????????????????,??????????????????
     * @Caching(evict = {
     *             @CacheEvict(value = "category", key="'getLevel1Categories'"),
     *             @CacheEvict(value = "category", key="'getCatalogJson'")
     *     })?????????????????????
     * @CacheEvict(value = "category", allEntries = true)??????????????????????????????
     * @param category
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "category", allEntries = true)
    public void updateCascade(CategoryEntity category) {
        updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }


    /**
     * ??????????????????????????????????????????????????????????????????
     * ??????sync = true????????????????????????????????????????????????????????????????????????????????????????????????
     * ?????????????????? ??????????????????????????????????????????????????????????????????
     * @return
     */
    @Override
    @Cacheable(value = {"category"}, key = "#root.method.name", sync = true)
    public List<CategoryEntity> getLevel1Categories() {
        return list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    @Override
    @Cacheable(value = {"category"}, key = "#root.method.name")
    public Map<String, List<Catalogs2Vo>> getCatalogJson() {
       return getCatalogJsonFromDB();
    }

    public Map<String, List<Catalogs2Vo>> getCatalogJsonFromDB() {
        System.out.println("??????????????????");
        // ??????????????????????????????????????????????????????
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);
        //1?????????????????????
        //1???1???????????????????????????
        List<CategoryEntity> level1Categories = getParentCid(selectList, 0L);
        //????????????
        Map<String, List<Catalogs2Vo>> parentCid = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1???????????????????????????,???????????????????????????????????????
            List<CategoryEntity> categoryEntities = getParentCid(selectList, v.getCatId());
            //2????????????????????????
            List<Catalogs2Vo> catalogs2Vos = null;
            if (categoryEntities != null) {
                catalogs2Vos = categoryEntities.stream().map(l2 -> {
                    Catalogs2Vo catalogs2Vo = new Catalogs2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());
                    //1????????????????????????????????????????????????vo
                    List<CategoryEntity> level3Catelog = getParentCid(selectList, l2.getCatId());
                    if (level3Catelog != null) {
                        List<Catalogs2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                            //2????????????????????????
                            Catalogs2Vo.Category3Vo category3Vo = new Catalogs2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return category3Vo;
                        }).collect(Collectors.toList());
                        catalogs2Vo.setCatalog3List(category3Vos);
                    }
                    return catalogs2Vo;
                }).collect(Collectors.toList());
            }
            return catalogs2Vos;
        }));
        return parentCid;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCascadeWithRedissonLock(CategoryEntity category) {
        //????????????
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("catalogJson-lock");
        RLock wLock = readWriteLock.writeLock();
        wLock.lock();
        try {
            updateById(category);
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
            redisTemplate.delete("category:getCatalogJson");
        }finally {
            wLock.unlock();
        }

    }

    /**
     * ??????????????????????????????db????????????
     * ?????????????????????????????????????????????
     * @return
     */
    public Map<String, List<Catalogs2Vo>> getCatalogJsonFromDbWithRedissonLock() {
        //????????????
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("catalogJson-lock");
        RLock rLock = readWriteLock.readLock();

        Map<String, List<Catalogs2Vo>> dataFromDb = null;
        rLock.lock();
        try {
            dataFromDb = getCatalogJsonFromDB();
        } finally {
            rLock.unlock();
        }
        return dataFromDb;
    }

    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList, Long parentCid) {
        return selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }


}