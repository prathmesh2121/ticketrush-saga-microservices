package com.ticketrush.inventory_service.Repository;

import com.ticketrush.inventory_service.Enum.SeatId;
import com.ticketrush.inventory_service.entity.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, SeatId> {
}
