package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.FileUploader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileUploader fileUploader;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> saveFile(@RequestPart("file") MultipartFile file,
                                               @RequestPart("name") String name) throws IOException {
        if(fileUploader.saveFile(file, name)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getFiles(@PathVariable String fileName) throws IOException {
        File file = fileUploader.getFile(fileName);

        if(file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] img = Files.readAllBytes(file.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(Files.probeContentType(file.toPath())));
        headers.setContentLength(img.length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(img);
    }
}
