package com.example.booking.repository;
import com.example.booking.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(value = "SELECT r FROM Room r " +
            "WHERE " +
            "r.name LIKE :search OR " +
            "r.description LIKE :search OR " +
            "r.type.name LIKE :search OR " +
            "EXISTS (SELECT 1 FROM RoomCategory rc WHERE rc.room = r AND rc.category.name LIKE :search)")

    Page<Room> searchEverything(String search, Pageable pageable);
}
