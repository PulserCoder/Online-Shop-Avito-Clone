package ru.skypro.homework.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.S3Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Сервис для работы с Amazon S3.
 * Предоставляет методы для загрузки файлов на S3, извлечения расширений файлов и создания клиентов S3.
 */
@Service
@Slf4j
public class S3ServiceImpl implements S3Service {

    @Value("${s3.selectel.accessKey}")
    private String accessKey;

    @Value("${s3.selectel.secretKey}")
    private String secretKey;

    @Value("${s3.selectel.bucketName}")
    private String bucketName;

    @Value("${s3.selectel.endpointApi}")
    private String endpointApi;

    /**
     * Загружает файл на S3 с использованием MultipartFile.
     *
     * @param file Файл для загрузки.
     * @param fileName Имя файла на S3.
     * @return true, если файл успешно загружен, иначе false.
     */
    @Override
    public boolean uploadFile(MultipartFile file, String fileName) {
        AmazonS3 s3Client = getS3Client();
        fileName = fileName + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            s3Client.putObject(
                    bucketName,
                    fileName,
                    convertToFile(file)
            );
            return true;
        } catch (AmazonServiceException | IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteFile(String link) {
        String fileName = link.substring(link.lastIndexOf('/') + 1);

        try {
            getS3Client().deleteObject(bucketName, fileName);
            return true;
        } catch (AmazonServiceException e) {
            log.error(e.getMessage());
            return false;
        }
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
     * Преобразует MultipartFile в обычный файл.
     *
     * @param multipartFile Файл для преобразования.
     * @return Обычный файл.
     * @throws IOException Если не удалось записать файл.
     */
    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

    /**
     * Создает клиента для работы с S3, используя предоставленные учетные данные и конфигурацию.
     *
     * @return Инстанс клиента AmazonS3.
     */
    private AmazonS3 getS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );

        AwsClientBuilder.EndpointConfiguration endpoint =
                new AwsClientBuilder.EndpointConfiguration(endpointApi, "ru-1");

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .build();
    }
}
