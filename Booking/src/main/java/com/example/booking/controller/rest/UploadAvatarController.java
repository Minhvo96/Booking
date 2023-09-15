package com.example.booking.controller.rest;


import com.example.booking.service.UploadService.response.ImageResponse;
import com.example.booking.service.room.RoomService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;


@RestController
@RequestMapping("api/upload")
@AllArgsConstructor
public class UploadAvatarController {
    RoomService roomService;
    @PostMapping
    public ResponseEntity<ImageResponse> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) {
        try {
            ImageResponse image = roomService.saveAvatar(avatar);
            return ResponseEntity.ok(image);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageResponse());
        }
    }

//    @PutMapping("{id}")
//    public ResponseEntity<ImageResponse> updateImage(@RequestParam("avatar") MultipartFile avatar, @PathVariable("id") String id) {
//        try {
//            ImageResponse image = roomService.updateImage(avatar, id);
//            return ResponseEntity.ok(image);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageResponse());
//        }
//    }

}