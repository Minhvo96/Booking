package com.example.booking.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "room_categories")
@Entity
@Data
@NoArgsConstructor
public class RoomCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Room room;

    @ManyToOne
    private Category category;

    public RoomCategory(Room room, Category category) {
        this.room = room;
        this.category = category;
    }
}
