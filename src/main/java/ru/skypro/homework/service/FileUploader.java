package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileUploader {
    boolean saveFile(MultipartFile file, String fileName) throws IOException;

    File getFile(String fileName) throws IOException;

    String getExtension(String filename);
}
