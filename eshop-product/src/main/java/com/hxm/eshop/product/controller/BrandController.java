package com.hxm.eshop.product.controller;

import java.util.Arrays;
import java.util.Map;

//mport org.apache.shiro.authz.annotation.RequiresPermissions;
import com.hxm.common.valid.AddGroup;
import com.hxm.common.valid.UpdateGroup;
import com.hxm.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hxm.eshop.product.entity.BrandEntity;
import com.hxm.eshop.product.service.BrandService;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.R;



/**
 * 品牌
 *
 * @author xiaoming
 * @date 2021-04-26 21:30:08
 */
@RefreshScope
@RestController
@RequestMapping("product/brand")
public class BrandController {

    @Value("${product.name}")
    private String name;

    @Value("${product.age}")
    private String age;

    @Autowired
    private BrandService brandService;

    @RequestMapping("/config")
    public R testConfig(){
        return R.ok().put("name",name).put("age",age);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand){
        brandService.updateDetail(brand);

        return R.ok();
    }
    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
