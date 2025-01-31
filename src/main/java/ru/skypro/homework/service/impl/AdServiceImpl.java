package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.AdMapperSimple;
import ru.skypro.homework.models.AdEntity;
import ru.skypro.homework.models.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.FileUploader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final AdMapperSimple adMapperSimple;
    private final UserRepository userRepository;
    private final FileUploader fileUploader;

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
    public boolean isAdOwner(int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity author = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        return author.getAds().stream().anyMatch(ad -> ad.getId() == id);

    }

    @Override
    public Ad createAd(CreateOrUpdateAd ad, MultipartFile file) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity author = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        AdEntity adEntity = adMapperSimple.toAdEntity(ad);
        adEntity.setAuthor(author);
        AdEntity newAd = adRepository.save(adEntity);
        String extention = fileUploader.getExtension(file.getOriginalFilename());
        String pathToImage = "/file/ad_" + newAd.getId() + "." + extention;
        if (fileUploader.saveFile(file, "ad_" + newAd.getId())) {
            adEntity.setImage(pathToImage);
        }
        else {
            throw new RuntimeException();
        }
        adRepository.save(newAd);
        return adMapperSimple.toAdDTO(adEntity);


    }

    @Override
    public ExtendedAd getAdDetailedById(int id) {
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
    public Ads getUserAds() {
        String author_name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity author = userRepository.findByEmail(author_name).orElseThrow(IllegalArgumentException::new);
        List<Ad> listAds = adMapperSimple.toAdListDTO(adRepository.findAllByAuthor(author));
        Ads ads = new Ads();
        ads.setCount(listAds.size());
        ads.setResults(listAds);
        return ads;


    }

    @Override
    public List<String> updateAdPhotoById(int id, MultipartFile file) throws IOException {
        String extention = fileUploader.getExtension(file.getOriginalFilename());
        AdEntity adEntity = adRepository.findById(id).orElseThrow(RuntimeException::new);
        String pathToImage = "/file/ad_" + adEntity.getId() + "." + extention;
        if (fileUploader.saveFile(file, "ad_" + adEntity.getId())) {
            adEntity.setImage(pathToImage);
        }
        else {
            throw new RuntimeException();
        }
        adRepository.save(adEntity);
        return new ArrayList<>(List.of(adEntity.getImage()));
    }

}
