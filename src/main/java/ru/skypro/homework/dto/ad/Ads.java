package ru.skypro.homework.dto.ad;

import lombok.Data;

import java.util.List;

@Data
public class Ads {
    private int count;
    private List<Ad> results;
}
