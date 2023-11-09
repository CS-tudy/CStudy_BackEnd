package com.cstudy.moduleapi.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Util {

    private final static String AWS_PROD_BUCKET_PATH  = "/Image";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${img.cloudFront}")
    String BASE_CLOUD_FRONT;


    private final AmazonS3Client amazonS3Client;


    public String uploadCompressedImage(MultipartFile file) {
        if (file == null || file.isEmpty())
            return "";

        byte[] originalImageBytes;
        try {
            originalImageBytes = file.getBytes();
        } catch (IOException e) {
            log.error("Error reading multipartFile", e);
            return "";
        }

        byte[] compressedImageBytes = ImageUtils.compressImage(originalImageBytes);

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + ".compressed";

        log.info("uploadCompressedImage fileName: {}", fileName);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(compressedImageBytes), null));

        return fileName;
    }





    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) return "";

        File fileObj = convertMultiPartFileToFile(file);
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID() + "." + extension;

        log.info("uploadFile fileName: {}", fileName);
        amazonS3Client.putObject(new PutObjectRequest(bucketName + AWS_PROD_BUCKET_PATH, fileName, fileObj));
        fileObj.delete();

        return fileName;
    }

    public String uploadFiles(List<MultipartFile> files) {
        // 다중 업로드 && 리스트 ","을 기준으로 하나의 문자열 반환
        // files 갯수 0 이면 반환 ""
        if (files == null || files.size() == 0)
            return "";

        StringBuilder mergedUrl = new StringBuilder();
        for (int i = 0; i < files.size(); i++) {
            mergedUrl.append(uploadFile(files.get(i)));
            if (i < files.size() - 1) {
                mergedUrl.append(",");
            }
        }
        log.info("uploadFiles mergedUrl: {}", mergedUrl);
        return mergedUrl.toString();
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = amazonS3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    private static String getFileExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }

}