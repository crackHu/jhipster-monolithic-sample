package com.hustack.sample.config;

import com.hustack.sample.service.util.RandomUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author crack
 * @date 2018/05/07
 */
@Configuration
public class RabbitMQConfiguration {

    public static final String EXCHANGE = "JhipsterMonolithicSample..exchange";

    public static final String QUEUE= "JhipsterMonolithicSample..queue";

    private ConnectionFactory connectionFactory;

    public RabbitMQConfiguration(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE,true,false);
    }

    @Bean
    public Queue queue(){
        return new Queue(QUEUE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(fanoutExchange());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setConfirmCallback(msgSendConfirmCallBack());
        return template;
    }

    public RabbitTemplate.ConfirmCallback msgSendConfirmCallBack(){
        return new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("回调id:" + correlationData);
                if (ack) {
                    System.out.println("消息成功消费");
                } else {
                    System.out.println("消息消费失败:" + cause +"\n重新发送");
                }
            }
        };
    }

    @RabbitListener(queues = QUEUE)
    public void processMessage(String content) {
        System.out.println("Received RabbitMQ Message: <" + content + ">");
    }
}
