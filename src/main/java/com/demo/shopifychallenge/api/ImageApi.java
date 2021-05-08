package com.demo.shopifychallenge.api;

import com.demo.shopifychallenge.model.Image;
import com.demo.shopifychallenge.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "management/api/v1")
@AllArgsConstructor
public class ImageApi {
    private final ImageService imageService;

    // Add image model
    // The  model is saved without the imageUrl in other to have an id generated
    @PostMapping("/add")
    public Image addImage(@RequestBody Image image) {
        return imageService.addImage(image);
    }

    // Get all images
    @GetMapping(path = "/all")
    public List<Image> getAllImages(){
        return imageService.getAllImages();
    }

    // Get one image
    @GetMapping(path = "{imageId}/image")
    public ResponseEntity<Image> getImage(@PathVariable(value = "imageId") Long imageId){
        return imageService.getImage(imageId);
    }

    // Update image model
    @PutMapping("/update")
    public Image updateImage(@RequestBody Image image) {
        return imageService.updateImage(image);
    }

    // Delete an image model
    @DeleteMapping("{imageId}/delete")
    public ResponseEntity<?> deleteImage(@PathVariable(value = "imageId") Long imageId){
        return imageService.deleteImage(imageId);
    }

    // Upload an image to the model
    // To upload an image, we first save the model and use the model id to save images to AWS s3
    @PostMapping(path = "{imageId}/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable(value = "imageId") Long imageId,
                                             @RequestParam(value = "file") MultipartFile file){
        return imageService.uploadImage(imageId,file);
    }

    // Download an image
    @GetMapping(path = "{imageId}/download")
    public byte[] downloadImage(@PathVariable(value = "imageId") Long imageId){

        return imageService.downloadImage(imageId);
    }
}
