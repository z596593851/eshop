package com.hxm.eshop.order.listener;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.hxm.eshop.order.config.AlipayTemplate;
import com.hxm.eshop.order.service.OrderService;
import com.hxm.eshop.order.vo.PayAsyncVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderPayedListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayTemplate alipayTemplate;

    @PostMapping(value = "/payed/notify")
    public String handleAlipayed(PayAsyncVo vo, HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
        System.out.println("实体:"+ JSONObject.toJSONString(vo));
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipayPublicKey(),
                alipayTemplate.getCharset(), alipayTemplate.getSignType()); //调用SDK验证签名

        if (signVerified) {
            System.out.println("签名验证成功...");
            //去修改订单状态

            String result = orderService.handlePayResult(vo);
            return result;
        } else {
            System.out.println("签名验证失败...");
            return "error";
        }
    }

}
