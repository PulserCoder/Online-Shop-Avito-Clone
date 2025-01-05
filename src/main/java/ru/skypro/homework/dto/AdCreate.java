package ru.skypro.homework.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdCreate {
    private MultipartFile image;
    private Ad properties;
}
