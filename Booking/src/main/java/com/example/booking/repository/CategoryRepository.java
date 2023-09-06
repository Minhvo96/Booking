package com.example.booking.repository;

import com.example.booking.domain.Category;
import com.example.booking.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
