package com.example.booking.controller.rest;
import com.example.booking.service.room.RoomService;
import com.example.booking.service.room.request.RoomSaveRequest;
import com.example.booking.service.room.response.RoomListResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("api/rooms")
@AllArgsConstructor
public class RoomRestController {

    private final RoomService roomService;
    
    @PostMapping
    public void create(@RequestBody RoomSaveRequest request){
        roomService.create(request);
    }

    @GetMapping
    public ResponseEntity<Page<RoomListResponse>> list(@PageableDefault(size = 5) Pageable pageable,
                                                       @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(roomService.getAll(pageable, search), HttpStatus.OK);
    }

}
