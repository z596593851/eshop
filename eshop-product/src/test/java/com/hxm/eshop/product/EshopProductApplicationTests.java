package com.hxm.eshop.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxm.eshop.product.entity.BrandEntity;
import com.hxm.eshop.product.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class EshopProductApplicationTests {

    class LRUCache {
        private final int MAX_CACHE_SIZE;
        private final float DEFAULT_LOAD_FACTORY = 0.75f;
        LinkedHashMap<Integer, Integer> map;

        public LRUCache(int capacity) {
            MAX_CACHE_SIZE = capacity;
            int ca = (int)Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTORY) + 1;
            map = new LinkedHashMap<Integer, Integer>(ca, DEFAULT_LOAD_FACTORY, true) {
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            };
        }

        public int get(int key) {
            return map.get(key);
        }

        public void put(int key, int value) {
            map.put(key, value);
        }
    }

    @Autowired
    BrandService brandService;

    @Test
    void insert() {
        BrandEntity brandEntity=new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
    }

    @Test
    void select(){
        List<BrandEntity> list= brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id",2));
        list.forEach(System.out::println);
    }

}
