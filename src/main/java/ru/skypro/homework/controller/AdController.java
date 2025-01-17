package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.*;
import ru.skypro.homework.service.AdService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("ads")
public class AdController {

    private final AdService adService;

    @GetMapping("/")
    public ResponseEntity<Ads> getAllAds() {
        try {
            return ResponseEntity.ok(adService.findAllWithCount());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(path="/", consumes = "multipart/form-data")
    public ResponseEntity<Ad> createAd(@RequestPart CreateOrUpdateAd ad,
                                       @RequestPart MultipartFile image) {
        // TODO:: save file in s3, create new ad by auth
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdDetailed> getAdById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(adService.getAdDetailedById(id));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ad> deleteAd(@PathVariable("id") int id) {
        // TODO:: Add checking by auth that this ad is user's
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
    public ResponseEntity<List<Ad>> getAllAdsMe() {
        // TODO:: do a realization of auth check and get all ads in service
        return null;
    }

    @PatchMapping(path="/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<String> updateAdImage(@PathVariable("id") int id, @RequestPart("image") MultipartFile image) {
        // TODO:: save file in s3, create new ad by auth also do a service method
        return null;
    }


}
