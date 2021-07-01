package com.hxm.eshop.ware.controller;

import java.util.Arrays;
import java.util.Map;

import com.hxm.eshop.ware.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hxm.eshop.ware.entity.WareInfoEntity;
import com.hxm.eshop.ware.service.WareInfoService;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.R;



/**
 * 仓库信息
 *
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-05-12 16:08:56
 */
@RestController
@RequestMapping("ware/wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    /**
     * 获取运费信息
     * @return
     */
    @GetMapping(value = "/fare")
    public R getFare(@RequestParam("addrId") Long addrId) {

        FareVo fare = wareInfoService.getFare(addrId);

        return R.ok().setData(fare);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
