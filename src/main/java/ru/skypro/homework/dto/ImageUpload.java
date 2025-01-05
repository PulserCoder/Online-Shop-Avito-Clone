package ru.skypro.homework.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class ImageUpload {
    private MultipartFile image;
}
