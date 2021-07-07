package com.hxm.eshop.order.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PayAsyncVo {

    //@JsonProperty(value = "gmt_create")
    private String gmt_create;

    private String charset;

    //@JsonProperty(value ="gmt_payment")
    private String gmt_payment;

    //@JSONField(name = "notify_time")
    private Date notify_time;

    private String subject;

    private String sign;

    //@JSONField(name = "buyer_id")
    private String buyer_id;//支付者的id

    //订单的信息
    private String body;

    //@JSONField(name = "invoice_amount")
    private String invoice_amount;//支付金额

    private String version;

    //@JSONField(name = "notify_id")
    private String notify_id;//通知id

    //@JSONField(name = "fund_bill_list")
    private String fund_bill_list;

    //@JSONField(name = "notify_type")
    private String notify_type;//通知类型； trade_status_sync

    //@JSONField(name = "out_trade_no")
    private String out_trade_no;//订单号

    //@JSONField(name = "total_amount")
    private String total_amount;//支付的总额

    //@JSONField(name = "trade_status")
    private String trade_status;//交易状态  TRADE_SUCCESS

    //@JSONField(name = "trade_no")
    private String trade_no;//流水号

    //@JSONField(name = "auth_app_id")
    private String auth_app_id;//

    //@JSONField(name = "receipt_amount")
    private String receipt_amount;//商家收到的款

    //@JSONField(name = "point_amount")
    private String point_amount;

    //@JSONField(name = "app_id")
    private String app_id;//应用id

    //@JSONField(name = "buyer_pay_amount")
    private String buyer_pay_amount;//最终支付的金额

    //@JSONField(name = "sign_type")
    private String sign_type;//签名类型

    //@JSONField(name = "seller_id")
    private String seller_id;//商家的id

}
