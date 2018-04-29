package com.hustack.sample.service;

import com.google.common.collect.Collections2;
import com.google.common.collect.Queues;
import com.hustack.sample.JhipsterMonolithicSampleApp;
import com.hustack.sample.service.dto.HotnewsDTO;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author crack
 * @date 2018/04/29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMonolithicSampleApp.class)
public class HotnewsIntServiceTest {

    @Autowired
    private HotnewsService hotnewsService;

    @Test
    public void batch() {

        long begin = System.currentTimeMillis();

        ExecutorService executorService = Executors.newCachedThreadPool();
        ConcurrentLinkedQueue<Integer> queue = Queues.newConcurrentLinkedQueue();
        List<Integer> collect = IntStream.range(0, 1000).boxed().collect(Collectors.toList());
        queue.addAll(collect);

        for (int i = 0; i < 10; i++) {
            Runnable run = () -> {
                boolean running = true;
                while (running) {
                    Integer poll = queue.poll();
                    if (poll != null) {
                        HotnewsDTO hotnewsDto = new HotnewsDTO();
                        hotnewsDto.setName(String.valueOf(poll));
                        hotnewsService.save(hotnewsDto);
                    } else {
                        running = false;
                    }
                }
            };
            executorService.submit(run);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }

        long end = System.currentTimeMillis();
        System.err.println("Coust: " + (end - begin) + "ms.");

    }

    public static void main(String[] args) {

        ConcurrentLinkedQueue<Integer> queue = Queues.newConcurrentLinkedQueue();
        List<Integer> collect = IntStream.range(0, 1000).boxed().collect(Collectors.toList());
        queue.addAll(collect);
        System.out.println(queue);

    }
}
