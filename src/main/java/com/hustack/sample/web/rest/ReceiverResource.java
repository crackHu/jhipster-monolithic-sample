package com.hustack.sample.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.CountDownLatch;

/**
 * @author crack
 * @date 2018/05/03
 */
@RequestMapping("/api")
public class ReceiverResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverResource.class);

    @Autowired
    StringRedisTemplate template;
    @Autowired
    CountDownLatch latch;

    @GetMapping("/receiver")
    public void receiver() {
        LOGGER.info("Sending message...");
        template.convertAndSend("chat", "Hello from Redis!");
        try {
            latch.await();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
