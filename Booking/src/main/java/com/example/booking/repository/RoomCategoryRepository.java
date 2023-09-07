package com.example.booking.repository;

import com.example.booking.domain.Room;
import com.example.booking.domain.RoomCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {
    @Transactional
    void deleteRoomCategoriesByRoomId(Long id);
}
