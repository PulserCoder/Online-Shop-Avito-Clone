package ru.skypro.homework.dto;


import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateAd {
    @NotBlank
    private String title;

    @Min(0)
    private int price;

    @Size(max = 1500)
    private String description;
}
