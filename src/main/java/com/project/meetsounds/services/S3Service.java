package com.project.meetsounds.services;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(@Value("${aws.access_key_id}") String accessKey,
                     @Value("${aws.secret_access_key}") String secretKey,
                     @Value("${aws.s3.region}") String region) {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        // Calculamos el hash (checksum) del archivo
        String fileChecksum = calculateChecksum(file.getInputStream());

        // Verificamos si el archivo ya existe en S3 usando el checksum como nombre de archivo
        if (s3Client.doesObjectExist(bucketName, fileChecksum)) {
            // Si ya existe, devolvemos la URL del archivo existente
            return s3Client.getUrl(bucketName, fileChecksum).toString();
        }

        // Si no existe, subimos el archivo a S3
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        s3Client.putObject(new PutObjectRequest(bucketName, fileChecksum, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // Devolvemos la URL del archivo subido
        return s3Client.getUrl(bucketName, fileChecksum).toString();
    }

    private String calculateChecksum(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        byte[] hash = digest.digest();
        return Base64.getEncoder().encodeToString(hash);  // O usar otra codificación
    }
}


