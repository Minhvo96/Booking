package com.example.booking.service.room.request;

import com.example.booking.service.request.SelectOptionRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomSaveRequest {
    private String name;

    private String price;

    private String description;

    private List<String> idCategories;

    private SelectOptionRequest type;
}
