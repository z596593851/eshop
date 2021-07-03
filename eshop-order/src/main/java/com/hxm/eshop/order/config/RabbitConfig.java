package com.hxm.eshop.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 死信队列，通过[order.release.order]路由跟[订单释放队列]绑定
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        /*
            Queue(String name,  队列名字
            boolean durable,  是否持久化
            boolean exclusive,  是否排他
            boolean autoDelete, 是否自动删除
            Map<String, Object> arguments) 属性
         */
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        // 消息过期时间 1分钟
        arguments.put("x-message-ttl", 60000);
        Queue queue = new Queue("order.delay.queue", true, false, false, arguments);

        return queue;
    }

    /**
     * 订单释放队列
     *
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {

        return new Queue("order.release.order.queue", true, false, false);
    }

    /**
     * TopicExchange
     *
     * @return
     */
    @Bean
    public Exchange orderEventExchange() {
        /*
         *   String name,
         *   boolean durable,
         *   boolean autoDelete,
         *   Map<String, Object> arguments
         * */
        return new TopicExchange("order-event-exchange", true, false);
    }

    /**
     * 将[order.create.order]路由和[死信队列]绑定
     * @return
     */
    @Bean
    public Binding orderCreateBinding() {
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                null);
    }

    /**
     * 将[order.release.order]路由和[订单释放队列]绑定
     * @return
     */
    @Bean
    public Binding orderReleaseBinding() {
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }

    /**
     * 将[order.release.other.#]路由和[库存释放队列]绑定
     * @return
     */
    @Bean
    public Binding orderReleaseOtherBinding() {

        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",
                null);
    }
}
