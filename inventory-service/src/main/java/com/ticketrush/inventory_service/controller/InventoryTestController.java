package com.ticketrush.inventory_service.controller;

import com.ticketrush.inventory_service.Enum.HoldResult;
import com.ticketrush.inventory_service.service.SeatHoldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryTestController
{
    public record SeatRequest(long eventId, String seatNo, String bookingId){}
    private final SeatHoldService service;

    public InventoryTestController(SeatHoldService service) {
        this.service = service;
    }

    @PostMapping("/hold")
    public ResponseEntity<Map<String, String>> hold(@RequestBody SeatRequest r)
    {
        HoldResult res = service.hold(r.eventId , r.seatNo, r.bookingId);
        HttpStatus ht = (res == HoldResult.HELD) ? HttpStatus.OK : HttpStatus.CONFLICT;
        return ResponseEntity.status(ht).body(Map.of("Result",res.name()));
    }

    @PostMapping("/release")
    public Map<String,Boolean> release(@RequestBody SeatRequest r)
    {
        return Map.of("Released",service.release(r.eventId(), r.seatNo(), r.bookingId()));
    }


}
