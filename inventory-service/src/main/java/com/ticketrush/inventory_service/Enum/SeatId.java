package com.ticketrush.inventory_service.Enum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @NoArgsConstructor
@AllArgsConstructor
public class SeatId implements Serializable
{
    private Long eventId;
    private String seatNo;

}
