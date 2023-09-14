package com.example.booking.util;

import com.cloudinary.utils.ObjectUtils;
import com.example.booking.domain.RoomAvatar;
import com.example.booking.exception.DataInputException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadUtils {
    public static final String IMAGE_UPLOAD_FOLDER = "c0323h1/product_images";

    public Map buildImageUploadParams(RoomAvatar roomAvatar) {
        if (roomAvatar == null || roomAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, roomAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

}
