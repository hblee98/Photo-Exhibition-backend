package com.hanbinlee.photoExhibition.controller;

import com.hanbinlee.photoExhibition.entity.Photo;
import com.hanbinlee.photoExhibition.service.CityService;
import com.hanbinlee.photoExhibition.service.PhotoService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);
    private final PhotoService photoService;
    private final CityService cityService;
    private final ResourceLoader resourceLoader;

    public PhotoController(PhotoService photoService, CityService cityService, ResourceLoader resourceLoader) {
        this.photoService = photoService;
        this.cityService = cityService;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/thumbnails")
    public List<Photo> getAllThumbnails() {
        List<Photo> photos = photoService.getAllPhotos();
        photos.forEach(photo -> {
            String[] pathParts = photo.getOriginFilePath().split("/");
            String fileName = pathParts[pathParts.length - 1];
            String thumbnailUrl = "/api/photos/images/" + photo.getRegion() + "/thumbnail/" + fileName;
            photo.setThumbnailFilePath(thumbnailUrl);
        });
        return photos;
    }

    @GetMapping("/regions")
    public List<String> getAllCities() {
        return cityService.getAllRegions();
    }

    @GetMapping("/regions/{region}")
    public List<Photo> getPhotosByRegion(@PathVariable String region) {
        return photoService.getPhotosByRegion(region);
    }

    @GetMapping("/images/{city}/thumbnail/{fileName}")
    public ResponseEntity<Resource> getThumbnail(@PathVariable("city") String city, @PathVariable("fileName") String fileName) {
        try {
            String resourcePath = String.format("classpath:static/images/%s/thumbnail/%s", city, fileName);
            logger.info("Loading thumbnail: {}", resourcePath);

            Resource resource = resourceLoader.getResource(resourcePath);
            
            if (resource.exists()) {
                String contentType = fileName.toLowerCase().endsWith(".jpg") || 
                                   fileName.toLowerCase().endsWith(".jpeg") ? 
                                   "image/jpeg" : "image/png";
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                logger.error("Thumbnail not found: {}", resourcePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error loading thumbnail: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/images/{city}/{fileName}")
    public ResponseEntity<Resource> getOriginal(@PathVariable("city") String city, @PathVariable("fileName") String fileName) {
        try {
            String resourcePath = String.format("classpath:static/images/%s/%s", city, fileName);
            logger.info("Loading original image: {}", resourcePath);

            Resource resource = resourceLoader.getResource(resourcePath);
            
            if (resource.exists()) {
                String contentType = fileName.toLowerCase().endsWith(".jpg") || 
                                   fileName.toLowerCase().endsWith(".jpeg") ? 
                                   "image/jpeg" : "image/png";
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                logger.error("Original image not found: {}", resourcePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error loading original image: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        try {
            photoService.deletePhoto(id);
            return ResponseEntity.ok().body("Successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error loading: " + e.getMessage());
        }
    }

}
