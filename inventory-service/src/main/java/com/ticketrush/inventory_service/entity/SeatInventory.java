package com.ticketrush.inventory_service.entity;

import com.ticketrush.inventory_service.Enum.SeatId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "seat_inventory")
@IdClass(SeatId.class) // this class has a composite primary key represented by a separate key class,
@Getter
@Setter
@NoArgsConstructor
public class SeatInventory {
    @Id
    @Column(name = "event_id") private Long eventId;
    @Id @Column(name = "seat_no") private String seatNo;
    private String status;
    @Column(name = "booking_id") private UUID bookingId;
    @Column(name = "sold_at") private OffsetDateTime soldAt;
}
