package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.models.AdEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AdService {
    Optional<AdEntity> findById(int id);

    List<AdEntity> findAll();

    Ad createAd(CreateOrUpdateAd ad, MultipartFile file) throws IOException;

    ExtendedAd getAdDetailedById(int id);

    Ad deleteAdById(int id);

    Ad updateAdById(CreateOrUpdateAd ad, int id);

    Ads getUserAds();

    List<String> updateAdPhotoById(int id, MultipartFile file) throws IOException;

    Ads findAllWithCount();

    boolean isAdOwner(int id);
}
