package com.hxm.eshop.order.feign;

import com.hxm.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("eshop-product")
public interface OrderFeignService {

    @RequestMapping("/product/brand/list")
    public R brandList();
}
