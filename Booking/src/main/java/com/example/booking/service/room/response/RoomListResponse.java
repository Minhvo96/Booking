package com.example.booking.service.room.response;
import com.example.booking.domain.RoomCategory;
import com.example.booking.domain.Type;
import com.example.booking.service.avatarService.RoomAvatarResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomListResponse {
    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String type;

    private String roomCategory;

    private RoomAvatarResponse avatar;
}
