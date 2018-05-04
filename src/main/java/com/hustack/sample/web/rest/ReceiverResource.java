package com.hustack.sample.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
