package com.hxm.eshop.order.web;

import com.alipay.api.AlipayApiException;
import com.hxm.eshop.order.config.AlipayTemplate;
import com.hxm.eshop.order.service.OrderService;
import com.hxm.eshop.order.vo.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class PayWebController {


    @Autowired
    private AlipayTemplate alipayTemplate;

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @GetMapping(value = "/aliPayOrder",produces = "text/html")
    public String aliPayOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {

        PayVo payVo = orderService.getOrderPay(orderSn);
        String pay = alipayTemplate.pay(payVo);
        System.out.println(pay);
        return pay;
    }
}
