package com.example.booking.service.room.request;

import com.example.booking.service.request.SelectOptionRequest;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomSaveRequest {
    private String name;

    private String price;

    private String description;

    private List<String> idCategories;

    private SelectOptionRequest type;

    private String avatar;
}
