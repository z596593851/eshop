package com.hxm.eshop.order;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class EshopOrderApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void send(){
        rabbitTemplate.convertAndSend("hello-exchange","hello.msg","hello world");
        log.info("消息发送完成");
    }

    @Test
    public void createExchange() {
        DirectExchange directExchange=new DirectExchange("hello-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("创建完成");
    }

    @Test
    public void createQueue() {
        Queue queue=new Queue("hello-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("queue创建完成");
    }

    @Test
    public void createBinding() {
        Binding binding=new Binding("hello-queue", Binding.DestinationType.QUEUE,"hello-exchange","hello.msg",null);
        amqpAdmin.declareBinding(binding);
        log.info("binding创建完成");
    }

}
