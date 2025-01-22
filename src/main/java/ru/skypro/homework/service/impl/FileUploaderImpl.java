package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.FileUploader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploaderImpl implements FileUploader {

    @Value("${file.directory}")
    private String STORAGE_DIRECTORY;

    /**
     * Сохраняет файл в указанную папку.
     *
     * @param file MultipartFile, который нужно сохранить.
     * @return Путь к сохранённому файлу.
     * @throws IOException Если файл не удалось сохранить.
     */
    @Override
    public boolean saveFile(MultipartFile file, String fileName) throws IOException {
        createDirectoryIfNotExists(STORAGE_DIRECTORY);

        fileName = fileName + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(STORAGE_DIRECTORY, fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Файл {} успешно сохранён в {}", fileName, filePath.toString());
        } catch (IOException e) {
            log.error("Ошибка при сохранении файла {}: {}", fileName, e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public File getFile(String fileName) throws IOException {
        Path filePath = Paths.get(STORAGE_DIRECTORY, fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("Файл " + fileName + " не найден в директории " + STORAGE_DIRECTORY);
        }

        log.info("Файл {} найден в директории {}", fileName, STORAGE_DIRECTORY);
        return filePath.toFile();
    }

    /**
     * Извлекает расширение файла.
     *
     * @param filename Имя файла.
     * @return Расширение файла.
     */
    @Override
    public String getExtension(String filename) {
        int index = filename.lastIndexOf(".");
        if (index == -1) {
            return "";
        }

        return filename.substring( index + 1);
    }

    /**
     * Создаёт директорию, если она не существует.
     *
     * @param directoryPath Путь к директории.
     * @throws IOException Если не удалось создать директорию.
     */
    private void createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Директория {} была создана.", directoryPath);
        }
    }
}

