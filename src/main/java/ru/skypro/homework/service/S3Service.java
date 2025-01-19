package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    boolean uploadFile(MultipartFile file, String fileName);

    boolean deleteFile(String link);

    String getExtension(String filename);
}
