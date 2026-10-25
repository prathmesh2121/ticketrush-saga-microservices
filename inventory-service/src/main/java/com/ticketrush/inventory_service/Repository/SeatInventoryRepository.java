package com.ticketrush.inventory_service.Repository;

import com.ticketrush.inventory_service.entity.SeatId;
import com.ticketrush.inventory_service.entity.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, SeatId>
{
    @Modifying
    @Query("""
    update SeatInventory s
       set s.status = 'SOLD', s.bookingId = :bookingId, s.soldAt = CURRENT_TIMESTAMP
     where s.eventId = :eventId and s.seatNo = :seatNo and s.status = 'AVAILABLE'
    """)
    int markSold(@Param("eventId") Long eventId,
                 @Param("seatNo") String seatNo,
                 @Param("bookingId") UUID bookingId);
}
