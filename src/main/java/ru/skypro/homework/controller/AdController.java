package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("ads")
public class AdController {
    @GetMapping("/")
    public ResponseEntity<Ads> getAllAds() {
        return null;
    }

    @PostMapping(path="/", consumes = "multipart/form-data")
    public ResponseEntity<Ad> createAd(@ModelAttribute AdCreate ad) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdDetailed> getAdById(@PathVariable("id") int id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ad> deleteAd(@PathVariable("id") int id) {
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable("id") int id, @RequestBody UpdateAd ad) {
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<List<Ad>> getAllAdsMe() {
        return null;
    }

    @PatchMapping(path="/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<String> updateAdImage(@PathVariable("id") int id, @RequestPart("image") MultipartFile image) {
        return null;
    }


}
