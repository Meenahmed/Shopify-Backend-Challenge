package com.demo.shopifychallenge.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FileStore {
    private final AmazonS3 amazonS3;

    public void save(String path, String fileName,
                     Optional<Map<String, String>> optionalMap,
                     InputStream inputStream){

        ObjectMetadata metadata = new ObjectMetadata();
        optionalMap.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });

        try{
            amazonS3.putObject(path, fileName, inputStream, metadata);
        }catch (AmazonServiceException e) {
            throw new IllegalStateException("Image could not be save to s3.");
        }
    }

    public byte[] download(String path, String key) {

        try{
            S3Object object = amazonS3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        }catch (AmazonServiceException | IOException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to Download from s3.");
        }
    }

}
