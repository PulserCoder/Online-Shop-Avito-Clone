package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.AdDetailed;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdMapperSimple;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final AdMapperSimple adMapperSimple;

    @Override
    public Optional<AdEntity> findById(int id) {
        return adRepository.findById(id);
    }

    @Override
    public List<AdEntity> findAll() {
        return adRepository.findAll();
    }

    @Override
    public Ads findAllWithCount() {
        List<AdEntity> listAdEntity = findAll();
        List<Ad> listAds = adMapperSimple.toAdListDTO(listAdEntity);
        Ads ads = new Ads();
        ads.setCount(listAdEntity.size());
        ads.setResults(listAds);
        return ads;
    }

    @Override
    public Ad createAd(CreateOrUpdateAd ad, MultipartFile file) {
        return null;
    }

    @Override
    public AdDetailed getAdDetailedById(int id) {
        AdEntity ad = adRepository.findById(id).orElseThrow(RuntimeException::new);
        return adMapper.toAdDetailed(ad);

    }

    @Override
    public Ad deleteAdById(int id) {
        AdEntity ad = adRepository.findById(id).orElseThrow(RuntimeException::new);
        adRepository.delete(ad);
        return adMapperSimple.toAdDTO(ad);
    }

    @Override
    public Ad updateAdById(CreateOrUpdateAd ad, int id) {
        AdEntity adEntity = adRepository.findById(id).orElseThrow(RuntimeException::new);
        adEntity.setPrice(ad.getPrice());
        adEntity.setDescription(ad.getDescription());
        adEntity.setTitle(ad.getTitle());
        adRepository.save(adEntity);
        return adMapperSimple.toAdDTO(adEntity);
    }

    @Override
    public List<Ad> getUserAds() {
        // TODO: HERE SHOULD BE ADS OF AUTH USER
        return List.of();
    }

    @Override
    public String updateAdPhotoById(int id, MultipartFile file) {
        // TODO: PUT PHOTO TO S3
        AdEntity adEntity = adRepository.findById(id).orElseThrow(RuntimeException::new);
        adEntity.setImage(file.getName());
        return file.getName();
    }

}
