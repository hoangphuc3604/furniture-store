package com.furnistyle.furniturebackend.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.furnistyle.furniturebackend.exceptions.NotFoundException;
import com.furnistyle.furniturebackend.services.CloudinaryService;
import com.furnistyle.furniturebackend.utils.Constants;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file)) {
            throw new IOException(Constants.Message.INVALID_FORMAT_IMAGES);
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException(Constants.Message.INVALID_FORMAT_IMAGES);
        }

        String baseFilename = originalFilename.contains(".")
            ? originalFilename.substring(0, originalFilename.lastIndexOf('.'))
            : originalFilename;

        String uniqueFilename = UUID.randomUUID() + "_" + baseFilename;

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "public_id", uniqueFilename,
            "folder", Constants.Message.CLOUD_FOLDER_NAME + "/"
        ));

        return uploadResult.get("url").toString();
    }

    private String extractPublicIdFromUrl(String fileUrl) {
        // URL https://res.cloudinary.com/<cloud_name>/image/upload/v<version>/<folder>/<public_id>.<format>
        try {
            int uploadIndex = fileUrl.indexOf("/upload/") + "/upload/".length();
            int extensionIndex = fileUrl.lastIndexOf(".");

            return fileUrl.substring(uploadIndex, extensionIndex);
        } catch (Exception e) {
            throw new IllegalArgumentException(Constants.Message.INVALID_URL);
        }
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        try {
            String publicId = extractPublicIdFromUrl(fileUrl);

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap()
            );

            if (!"ok".equals(result.get("result"))) {
                throw new NotFoundException(Constants.Message.NOT_FOUND_CLOUD_FILE);
            }
        } catch (Exception e) {
            throw new IOException(Constants.Message.DELETE_CLOUD_FILE_FAILED);
        }
    }
}
