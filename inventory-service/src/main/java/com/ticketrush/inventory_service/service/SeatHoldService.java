package com.ticketrush.inventory_service.service;

import com.ticketrush.inventory_service.entity.SeatId;
import com.ticketrush.inventory_service.Enum.HoldResult;
import com.ticketrush.inventory_service.Repository.SeatInventoryRepository;
import com.ticketrush.inventory_service.entity.SeatInventory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatHoldService
{
    private static final long HOLD_TTL_SECONDS = 300;
    private final StringRedisTemplate redis;
    private final RedisScript<Long> acquireScript;   // names match the @Bean methods
    private final RedisScript<Long> releaseScript;
    private final SeatInventoryRepository repository;

    public SeatHoldService(StringRedisTemplate redis, RedisScript<Long> acquireScript, RedisScript<Long> releaseScript, SeatInventoryRepository repository) {
        this.redis = redis;
        this.acquireScript = acquireScript;
        this.releaseScript = releaseScript;
        this.repository = repository;
    }

    public HoldResult hold(long eventId, String seatNo, String bookingId)
    {
        SeatInventory seat = repository.findById(new SeatId(eventId, seatNo)).orElse(null);

        if(seat == null)
        {
            return HoldResult.SEAT_NOT_FOUND;
        }

        if("SOLD".equals(seat.getStatus()))
        {
            return HoldResult.ALREADY_SOLD;
        }

        Long result = redis.execute(
                acquireScript,                                  // 1st argument : script
                List.of(key(eventId, seatNo)),                  // 2nd : KEYS
                bookingId,                                      // 3rd : 1st ARGV
                String.valueOf(HOLD_TTL_SECONDS));              // 4th : 2nd ARGV

        return Long.valueOf(1).equals(result) ? HoldResult.HELD : HoldResult.HELD_BY_OTHER;
    }

    public boolean release(long eventId, String seatNo, String bookingId)
    {
        Long result = redis.execute(releaseScript,
                List.of(key(eventId, seatNo)), bookingId);
        // construct a key Eg. "seat:42:A3" & pass bookingId Eg "BK5847235"
        // For example, if seat:42:A3 stores BK5847235 in Redis, then release(42, "A3", "BK5847235") means delete.
        // If deleted successfully -> then result = 1 -> return TRUE
        // if not found and not deleted -> result = 0 -> return FALSE

        //Lua arrays are 1-indexed, meaning their first element is at index 1, not 0.

        return Long.valueOf(1).equals(result);
    }

    private String key(long eventId, String seatNo) {
        return "seat:" + eventId + ":" + seatNo;
    }
}
