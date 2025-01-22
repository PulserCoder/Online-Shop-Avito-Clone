package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.UserEntity;

import java.util.List;

public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    List<AdEntity> findAllByAuthor(UserEntity author);
}
