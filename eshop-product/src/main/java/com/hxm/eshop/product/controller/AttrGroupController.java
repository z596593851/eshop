package com.hxm.eshop.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//mport org.apache.shiro.authz.annotation.RequiresPermissions;
import com.hxm.eshop.product.entity.AttrEntity;
import com.hxm.eshop.product.service.AttrAttrgroupRelationService;
import com.hxm.eshop.product.service.AttrService;
import com.hxm.eshop.product.service.CategoryService;
import com.hxm.eshop.product.vo.AttrGroupRelationVo;
import com.hxm.eshop.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hxm.eshop.product.entity.AttrGroupEntity;
import com.hxm.eshop.product.service.AttrGroupService;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.R;



/**
 * 属性分组
 *
 * @author xiaoming
 * @date 2021-04-26 21:30:08
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;
    @Autowired
    AttrAttrgroupRelationService relationService;

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId")Long catelogId){

        //1、查出当前分类下的所有属性分组，
        //2、查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos =  attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable ("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }

    /**
     * 新增分组和属性的关联
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){

        relationService.saveBatch(vos);
        return R.ok();
    }

    /**
     * 删除分组和属性的关联
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody  List<AttrGroupRelationVo> vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 根据分组id查找关联的所有基本属性
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entities =  attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    /**
     * 获取当前分组没有关联的所有属性
     * @param attrgroupId
     * @param params
     * @return
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
		List<Long> path=categoryService.findCatelogPath(attrGroup.getCatelogId());
		attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
