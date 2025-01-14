package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Ad;

public interface AdRepository extends JpaRepository<Ad, Integer> {
}
