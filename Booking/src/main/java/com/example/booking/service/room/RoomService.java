package com.example.booking.service.room;
import com.example.booking.domain.Category;
import com.example.booking.domain.Room;
import com.example.booking.domain.RoomCategory;
import com.example.booking.domain.Type;
import com.example.booking.repository.RoomCategoryRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.service.room.request.RoomSaveRequest;
import com.example.booking.service.room.response.RoomDetailResponse;
import com.example.booking.service.room.response.RoomListResponse;
import com.example.booking.util.AppUtil;
import jakarta.transaction.Transactional;
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
    public RoomDetailResponse findById(Long id){
        var room = roomRepository.findById(id).orElse(new Room());
        var result = AppUtil.mapper.map(room, RoomDetailResponse.class);
        result.setTypeId(room.getType().getId());
        result.setCategoryIds(room
                .getRoomCategories()
                .stream().map(roomCategory -> roomCategory.getCategory().getId())
                .collect(Collectors.toList()));
        return result;
    }
    public void update(RoomSaveRequest request, Long id){
        var roomDb = roomRepository.findById(id).orElse(new Room());
        roomDb.setType(new Type());
        AppUtil.mapper.map(request,roomDb);
        roomCategoryRepository.deleteAll(roomDb.getRoomCategories());

        var roomCategories = new ArrayList<RoomCategory>();
        for (String idCategory : request.getIdCategories()) {
            Category category = new Category(Long.valueOf(idCategory));
            roomCategories.add(new RoomCategory(roomDb, category));
        }
        roomCategoryRepository.saveAll(roomCategories);
        roomRepository.save(roomDb);
    }

    public Boolean delete(Long id) {
        roomCategoryRepository.deleteRoomCategoriesByRoomId(id);
        roomRepository.deleteById(id);
        return true;
    }




}


