package com.hxm.eshop.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxm.eshop.product.entity.BrandEntity;
import com.hxm.eshop.product.service.BrandService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EshopProductApplicationTests {

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
