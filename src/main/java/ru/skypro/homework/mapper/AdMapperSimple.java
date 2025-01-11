package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.ad.Ad;

@Mapper
public interface AdMapperSimple {
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "id", target = "pk")
    Ad toAdDTO(ru.skypro.homework.models.Ad ad);
}
