package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.models.AdEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdMapperSimple {
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    Ad toAdDTO(AdEntity ad);


    AdEntity toAdEntity(CreateOrUpdateAd ad);


    List<Ad> toAdListDTO(List<AdEntity> ads);
}
