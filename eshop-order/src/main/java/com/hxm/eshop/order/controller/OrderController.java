package com.hxm.eshop.order.controller;

import java.util.Arrays;
import java.util.Map;

//mport org.apache.shiro.authz.annotation.RequiresPermissions;
import com.hxm.eshop.order.feign.ProductFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hxm.eshop.order.entity.OrderEntity;
import com.hxm.eshop.order.service.OrderService;
import com.hxm.common.utils.PageUtils;
import com.hxm.common.utils.R;



/**
 * 订单
 *
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-04-27 16:26:19
 */
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    ProductFeignService productFeignService;

    /**
     * 根据订单编号查询订单状态
     * @param orderSn
     * @return
     */
    @GetMapping(value = "/status/{orderSn}")
    public R getOrderStatus(@PathVariable("orderSn") String orderSn) {
        OrderEntity orderEntity = orderService.getOrderByOrderSn(orderSn);
        return R.ok().setData(orderEntity);
    }

    @RequestMapping("/brandList")
    public R brandList(){
        R result=productFeignService.brandList();
        return R.ok().put("page",result.get("page"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
