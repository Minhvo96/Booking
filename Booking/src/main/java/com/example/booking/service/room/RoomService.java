package com.example.booking.service.room;

import com.example.booking.domain.Category;
import com.example.booking.domain.Room;
import com.example.booking.domain.RoomCategory;
import com.example.booking.repository.RoomCategoryRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.service.room.request.RoomSaveRequest;
import com.example.booking.service.room.response.RoomListResponse;
import com.example.booking.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final RoomCategoryRepository roomCategoryRepository;

    public void create(RoomSaveRequest request){
        var room = AppUtil.mapper.map(request, Room.class);
        room = roomRepository.save(room);
//        var roomCategories = new ArrayList<RoomCategory>();
//        for (var id: request.getIdCategories()) {
//            roomCategories.add(new RoomCategory(room, new Category(Long.valueOf(id))));
//        }
//        roomCategoryRepository.saveAll(roomCategories);
        Room finalRoom = room;
        roomCategoryRepository.saveAll(request
                .getIdCategories()
                .stream()
                .map(id -> new RoomCategory(finalRoom, new Category(Long.valueOf(id))))
                .collect(Collectors.toList()));
    }

    public Page<RoomListResponse> getAll(Pageable pageable, String search) {
        search = "%" + search + "%";
        return roomRepository.searchEverything(search, pageable).map(room ->
                    RoomListResponse.builder()
                    .id(room.getId())
                    .name(room.getName())
                            .description(room.getDescription())
                    .price(room.getPrice())
                    .type(room.getType().getName())
                    .roomCategory(
                            room.getRoomCategories().stream()
                                    .map(roomCategory -> roomCategory.getCategory().getName())
                                    .collect(Collectors.joining(", ")) // Chuyển danh sách thành chuỗi
                    )
                    .build());
    }


}


