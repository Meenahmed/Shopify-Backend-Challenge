package com.demo.shopifychallenge.service;

import com.amazonaws.services.securityhub.model.Product;
import com.demo.shopifychallenge.bucket.Bucket;
import com.demo.shopifychallenge.exception.ApiResponse;
import com.demo.shopifychallenge.exception.ResourcesNotFoundException;
import com.demo.shopifychallenge.filestore.FileStore;
import com.demo.shopifychallenge.model.Image;
import com.demo.shopifychallenge.repository.ImageRepository;
import com.demo.shopifychallenge.util.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileStore fileStore;
    private final Validator validator;

    // Add new Image
    public Image addImage(Image image){
        image.setCreatedAt(Instant.now());
        return imageRepository.save(image);
    }

    // Get all images
    public List<Image> getAllImages(){
        return imageRepository.findAll();
    }

    // Update image
    public Image updateImage(Image imageRequest) {
        return imageRepository.findById(imageRequest.getId()).map((image) ->{
            image.setUpdateAt(Instant.now());
            image.setImagePrice(imageRequest.getImagePrice());
            image.setName(imageRequest.getName());
            image.setDescription(imageRequest.getDescription());
            return  imageRepository.save(image);
        }).orElseThrow(() -> new ResourcesNotFoundException("image with the id not updated" + imageRequest.getId()));

    }

    // Get one image
    public ResponseEntity<Image> getImage(Long imageId){
        Optional<Image> image = imageRepository.findById(imageId);
        if(!image.isPresent())
            return new ResponseEntity(new ApiResponse(false, "Image not found"),
                    HttpStatus.NOT_FOUND);

        return ResponseEntity.of(image);
    }

    // Delete an Image
    public ResponseEntity<?> deleteImage(Long imageId){
        Optional<Image> image = imageRepository.findById(imageId);
        if(!image.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false,"Image not found"),
                    HttpStatus.NOT_FOUND);
        }
        imageRepository.delete(image.get());

        return ResponseEntity.ok().body(
                new ApiResponse(true, "Image successfully deleted"));
    }

    // Upload product image
    public ResponseEntity<?> uploadImage(Long imageId, MultipartFile file) {
        if(file.getOriginalFilename().isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false, "No file is uploaded"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!validator.validateFile(file.getOriginalFilename())) {
            return new ResponseEntity<>(new ApiResponse(false, "Image must be in jpeg|png|bmp format."),
                    HttpStatus.BAD_REQUEST);
        }


        Image image = imageRepository.getOne(imageId);
        if(image.getId() == null){
            return new ResponseEntity<>(new ApiResponse(false, "Image not found"),
                    HttpStatus.NOT_FOUND);
        }

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", file.getContentType());
        map.put("Size", String.valueOf(file.getSize()));
        map.put("Name", file.getOriginalFilename());

        String path = String.format("%s/%s", Bucket.PRODUCT_IMAGE.getName(), image.getId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, fileName, Optional.of(map), file.getInputStream());

            //Setting imageUrl
            image.setImageurl(fileName);
            imageRepository.save(image);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Something went wrong"),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(new ApiResponse(true, "Image successfully saved to s3."));
    }

    // Download image
    public byte[] downloadImage(Long imageId){
        Image image = imageRepository.getOne(imageId);
        if(image.getId() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Image not found");
        }

        String path = String.format("%s/%s", Bucket.PRODUCT_IMAGE.getName(), image.getId());
        return image.getImageurl().map(key -> fileStore.download(path,key)).orElse(new byte[0]);

    }

}
