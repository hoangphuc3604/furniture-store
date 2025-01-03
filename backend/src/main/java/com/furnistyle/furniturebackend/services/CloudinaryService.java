package com.furnistyle.furniturebackend.services;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CloudinaryService {
    String storeFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;
}
