package com.hustack.sample.web.rest;

import com.hustack.sample.config.RabbitMQConfiguration;
import com.hustack.sample.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author crack
 * @date 2018/05/03
 */
@RestController
@RequestMapping("/api")
public class ReceiverResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverResource.class);

    @Autowired
    StringRedisTemplate template;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    CountDownLatch latch;

    @GetMapping("/receiver")
    public ResponseEntity<String> receiver() {
        LOGGER.info("Sending message...{}", latch.getCount());
        template.convertAndSend("chat", "Hello from Redis!");
        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/receiver2")
    public ResponseEntity<String> receiver2() {
        CorrelationData correlationData = new CorrelationData(RandomUtil.generatePassword());
        LOGGER.info("Sending {} ack message...", correlationData);
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.QUEUE, "Hello from RabbitMQ0!");
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE, "","Hello from RabbitMQ1!", correlationData);
        amqpTemplate.convertAndSend(RabbitMQConfiguration.QUEUE, "Hello from RabbitMQ4!");
        amqpTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE, "","Hello from RabbitMQ3!");
        return ResponseEntity.ok("ok");
    }
}
