package ru.skypro.homework.service;

import ru.skypro.homework.models.Ad;

import java.util.Optional;

public interface AdService {
    Optional<Ad> findById(int id);
}
