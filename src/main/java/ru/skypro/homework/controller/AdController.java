package ru.skypro.homework.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.*;
import ru.skypro.homework.service.AdService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("ads")
public class AdController {

    private final AdService adService;
    private final ObjectMapper objectMapper;


    @GetMapping("")
    public ResponseEntity<Ads> getAllAds() {
        try {
            return ResponseEntity.ok(adService.findAllWithCount());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ad> createAd(@RequestPart(value = "properties") String adJson,
                                       @RequestPart(value = "image") MultipartFile image) {
        try {
            CreateOrUpdateAd ad = objectMapper.readValue(adJson, CreateOrUpdateAd.class);
            Ad adObj = adService.createAd(ad, image);
            return ResponseEntity.ok(adObj);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok().body(adService.getAdDetailedById(id));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN') or adServiceImpl.isAdOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Ad> deleteAd(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(adService.deleteAdById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable("id") int id, @RequestBody CreateOrUpdateAd ad) {
        // TODO:: Add checking by auth that this ad is user's
        try {
            return ResponseEntity.ok(adService.updateAdById(ad, id));
        } catch (RuntimeException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAllAdsMe() {
        return ResponseEntity.ok(adService.getUserAds());
    }

    @PatchMapping(path = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<String> updateAdImage(@PathVariable("id") int id, @RequestPart("image") MultipartFile image) {
        // TODO:: save file in s3, create new ad by auth also do a service method
        return null;
    }


}
