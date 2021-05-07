package com.hxm.eshop.product.controller;

import java.util.Arrays;
import java.util.Map;

//mport org.apache.shiro.authz.annotation.RequiresPermissions;
import com.hxm.eshop.product.vo.AttrRespVo;
import com.hxm.eshop.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hxm.eshop.product.entity.AttrEntity;
import com.hxm.eshop.product.service.AttrService;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.R;



/**
 * 商品属性
 *
 * @author xiaoming
 * @email sunlightcs@gmail.com
 * @date 2021-04-26 21:30:08
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType")String type){

        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,type);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
        AttrRespVo respVo = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
        attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
