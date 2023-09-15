package com.example.booking.service.room.response;

import com.example.booking.service.UploadService.response.ImageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class RoomDetailResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private Long typeId;
    private List<Long> categoryIds;
    private ImageResponse image;
}
