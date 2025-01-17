package ru.skypro.homework.mapper.utils;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.skypro.homework.models.Ad;
import ru.skypro.homework.service.AdService;

@Named("CommentMapperUtils")
@Component
@RequiredArgsConstructor
public class CommentMappersUtils {

    private final AdService adService;


    @Named("getAdById")
    public Ad getAdById(int adId) {
        return adService.findById(adId).orElseThrow(() -> new IllegalArgumentException("Ad not found with id: "
                + adId));
    }


}
