package com.example.booking.service.room;

import com.example.booking.domain.*;
import com.example.booking.repository.RoomAvatarRepository;
import com.example.booking.repository.RoomCategoryRepository;
import com.example.booking.repository.RoomRepository;
import com.example.booking.service.UploadService.UploadService;
import com.example.booking.service.UploadService.response.ImageResponse;
import com.example.booking.service.room.request.RoomSaveRequest;
import com.example.booking.service.room.response.RoomDetailResponse;
import com.example.booking.service.room.response.RoomListResponse;
import com.example.booking.util.AppUtil;
import com.example.booking.util.UploadUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomAvatarRepository roomAvatarRepository;
    private final UploadService uploadService;
    private final UploadUtils uploadUtils;

//    public Page<RoomListResponse> getAll(Pageable pageable, String search) {
//        search = "%" + search + "%";
//        return roomRepository.searchEverything(search, pageable).map(room ->
//                RoomListResponse.builder()
//                        .id(room.getId())
//                        .name(room.getName())
//                        .description(room.getDescription())
//                        .price(room.getPrice())
//                        .type(room.getType().getName())
//                        .roomCategory(
//                                room.getRoomCategories().stream()
//                                        .map(roomCategory -> roomCategory.getCategory().getName())
//                                        .collect(Collectors.joining(", ")) // Chuyển danh sách thành chuỗi
//                        )
//                        .build());
//    }

    public Page<RoomListResponse> getAll(Pageable pageable, String search) {
        search = "%" + search + "%";
        return roomRepository.searchEverything(search, pageable).map(e -> {
            var result = AppUtil.mapper.map(e, RoomListResponse.class);
            result.setType(e.getType().getName());
            result.setRoomCategory(e.getRoomCategories()
                    .stream().map(c -> c.getCategory().getName())
                    .collect(Collectors.joining(", ")));
            return result;
        });
    }

    public void create(RoomSaveRequest request) throws IOException {
//        RoomAvatar roomAvatar = new RoomAvatar();
//
//        roomAvatarRepository.save(roomAvatar);
//
//        Map uploadResult =  uploadService.uploadImage(request.getAvatar(), uploadUtils.buildImageUploadParams(roomAvatar));
//
//        String fileUrl = (String) uploadResult.get("secure_url");
//        String fileFormat = (String) uploadResult.get("format");
//
//        roomAvatar.setFileName(roomAvatar.getId() + "." + fileFormat);
//        roomAvatar.setFileUrl(fileUrl);
//        roomAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
//        roomAvatar.setCloudId(roomAvatar.getFileFolder() + "/" + roomAvatar.getId());
//        roomAvatarRepository.save(roomAvatar);

        var room = AppUtil.mapper.map(request, Room.class);
        room.setAvatar(new RoomAvatar(request.getAvatar()));
        room = roomRepository.save(room);

        Room finalRoom = room;
        roomCategoryRepository.saveAll(request
                .getIdCategories()
                .stream()
                .map(id -> new RoomCategory(finalRoom, new Category(Long.valueOf(id))))
                .collect(Collectors.toList()));
    }

    public RoomDetailResponse findById(Long id) {
        var room = roomRepository.findById(id).orElse(new Room());
        var result = AppUtil.mapper.map(room, RoomDetailResponse.class);
        result.setImage(new ImageResponse(room.getAvatar().getId(),room.getAvatar().getFileUrl()));
        result.setTypeId(room.getType().getId());
        result.setCategoryIds(room
                .getRoomCategories()
                .stream().map(roomCategory -> roomCategory.getCategory().getId())
                .collect(Collectors.toList()));
        return result;
    }

    public void update(RoomSaveRequest request, Long id) {
        var roomDb = roomRepository.findById(id).orElse(new Room());
        roomDb.setType(new Type());
        AppUtil.mapper.map(request, roomDb);
        roomCategoryRepository.deleteAll(roomDb.getRoomCategories());

        var imageId = roomDb.getAvatar().getId();
        roomDb.setAvatar(null);
        roomAvatarRepository.deleteById(imageId);

        var roomCategories = new ArrayList<RoomCategory>();
        for (String idCategory : request.getIdCategories()) {
            Category category = new Category(Long.valueOf(idCategory));
            roomCategories.add(new RoomCategory(roomDb, category));
        }
        roomDb.setAvatar(new RoomAvatar(request.getAvatar()));
        roomCategoryRepository.saveAll(roomCategories);
        roomRepository.save(roomDb);
    }

    public Boolean delete(Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            roomCategoryRepository.deleteRoomCategoriesByRoomId(id);
            roomRepository.deleteById(id);
            return true;
        } else {
            return false; // Không tìm thấy phòng để xóa
        }
    }
    public ImageResponse saveAvatar(MultipartFile avatar) throws IOException {
        RoomAvatar roomAvatar = new RoomAvatar();
        roomAvatarRepository.save(roomAvatar);

        Map uploadResult = uploadService.uploadImage(avatar, uploadUtils.buildImageUploadParams(roomAvatar));

        String fileUrl = (String) uploadResult.get("secure_url");
        String fileFormat = (String) uploadResult.get("format");

        roomAvatar.setFileName(roomAvatar.getId() + "." + fileFormat);
        roomAvatar.setFileUrl(fileUrl);
        roomAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
        roomAvatar.setCloudId(roomAvatar.getFileFolder() + "/" + roomAvatar.getId());
        roomAvatarRepository.save(roomAvatar);
        return new ImageResponse(roomAvatar.getId(), roomAvatar.getFileUrl());
    }

//    public ImageResponse updateImage(MultipartFile avatar, String id) throws IOException {
//        roomAvatarRepository.deleteById(Long.valueOf(id));
//
//        RoomAvatar roomAvatar = new RoomAvatar();
//        roomAvatarRepository.save(roomAvatar);
//
//        Map uploadResult = uploadService.uploadImage(avatar, uploadUtils.buildImageUploadParams(roomAvatar));
//
//        String fileUrl = (String) uploadResult.get("secure_url");
//        String fileFormat = (String) uploadResult.get("format");
//
//        roomAvatar.setFileName(roomAvatar.getId() + "." + fileFormat);
//        roomAvatar.setFileUrl(fileUrl);
//        roomAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
//        roomAvatar.setCloudId(roomAvatar.getFileFolder() + "/" + roomAvatar.getId());
//        roomAvatarRepository.save(roomAvatar);
//        return new ImageResponse(roomAvatar.getId(), roomAvatar.getFileUrl());
//    }

}


