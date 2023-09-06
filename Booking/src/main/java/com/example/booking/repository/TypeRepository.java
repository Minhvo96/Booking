package com.example.booking.repository;

import com.example.booking.domain.Room;
import com.example.booking.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {
}
