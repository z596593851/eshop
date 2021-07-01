package com.hxm.eshop.order.web;

import com.hxm.common.exception.NoStockException;
import com.hxm.eshop.order.service.OrderService;
import com.hxm.eshop.order.vo.OrderConfirmVo;
import com.hxm.eshop.order.vo.OrderSubmitVo;
import com.hxm.eshop.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {

    @Autowired
    private OrderService orderService;

    /**
     * 去结算确认页
     * @param model
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping(value = "/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {

        OrderConfirmVo confirmVo = orderService.confirmOrder();

        model.addAttribute("confirmOrderData",confirmVo);
        //展示订单确认的数据

        return "confirm";
    }


    /**
     * 下单功能
     * @param vo
     * @return
     */
    @PostMapping(value = "/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes attributes) {

        try {
            SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
            //下单成功来到支付选择页
            //下单失败回到订单确认页重新确定订单信息
            if (responseVo.getCode() == 0) {
                //成功
                model.addAttribute("submitOrderResp",responseVo);
                return "pay";
            } else {
                String msg = "下单失败";
                switch (responseVo.getCode()) {
                    case 1: msg += "令牌订单信息过期，请刷新再次提交"; break;
                    case 2: msg += "订单商品价格发生变化，请确认后再次提交"; break;
                    case 3: msg += "库存锁定失败，商品库存不足"; break;
                    default:break;
                }
                attributes.addFlashAttribute("msg",msg);
                return "redirect:http://order.eshop.com/toTrade";
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof NoStockException) {
                String message = ((NoStockException)e).getMessage();
                attributes.addFlashAttribute("msg",message);
            }
            return "redirect:http://order.eshop.com/toTrade";
        }
    }

}
