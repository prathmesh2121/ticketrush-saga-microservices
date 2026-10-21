package com.ticketrush.inventory_service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatInventoryRepository extends JpaRepository<SeatInventory, SeatId> {
}
