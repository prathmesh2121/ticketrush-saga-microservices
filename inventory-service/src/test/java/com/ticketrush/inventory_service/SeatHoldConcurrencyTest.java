package com.ticketrush.inventory_service;
import com.ticketrush.inventory_service.Enum.HoldResult;
import com.ticketrush.inventory_service.service.SeatHoldService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SeatHoldConcurrencyTest
{
    @Autowired
    SeatHoldService service;

    @Autowired
    StringRedisTemplate redis;

    @BeforeEach
    @AfterEach
    void cleanup()
    {
        redis.delete(List.of("seat:1:A50","seat:1:A51"));
    }

    @Test
    void onlyOneUserWinsTheSeat() throws Exception
    {
        int users = 50;
        ExecutorService pool = Executors.newFixedThreadPool(users);
        CountDownLatch ready = new CountDownLatch(users);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<HoldResult>> futures = new ArrayList<>();

        for(int i=0; i<users; i++)
        {
            String bookingId = UUID.randomUUID().toString();
            futures.add(pool.submit(()->{
                ready.countDown();
                start.await();
                return service.hold(1L,"A50",bookingId);
            }));
        }
        ready.await();

        //starting gun. It releases all 50 threads at nearly the same moment.
        start.countDown();
        
        int held = 0;
        for (Future<HoldResult> f : futures)
        {
            if (f.get() == HoldResult.HELD) held++;
        }
        pool.shutdown();

        assertEquals(1, held);
    }
    @Test
    void releaseWorksOnlyForTheOwner()
    {
        assertEquals(HoldResult.HELD, service.hold(1L,"A51", "b1"));
        assertFalse(service.release(1L, "A51", "b2"));
        assertEquals(HoldResult.HELD_BY_OTHER, service.hold(1L,"A51", "b3"));
        assertTrue(service.release(1L, "A51", "b1"));
        assertEquals(HoldResult.HELD, service.hold(1L,"A51", "b3"));
    }
}
