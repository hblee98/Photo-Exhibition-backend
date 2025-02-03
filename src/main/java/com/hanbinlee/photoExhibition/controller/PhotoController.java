package com.hanbinlee.photoExhibition.controller;

import com.hanbinlee.photoExhibition.entity.Photo;
import com.hanbinlee.photoExhibition.service.CityService;
import com.hanbinlee.photoExhibition.service.PhotoService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final CityService cityService;
    public PhotoController(PhotoService photoService, CityService cityService) {
        this.photoService = photoService;
        this.cityService = cityService;
    }
    @GetMapping("/all")
    public List<Photo> getAllPhotos() {return photoService.getAllPhotos();}
    @GetMapping("/regions/{region}")
    public List<Photo> getPhotosByRegion(@PathVariable String region) {
        return photoService.getPhotosByRegion(region);
    }
    @GetMapping("/regions")
    public List<String> getAllCities(){
        return cityService.getAllRegions();
    }

    @GetMapping("/{city}/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String city, @PathVariable String fileName) throws IOException {
        Path imagePath = Paths.get("src/main/resources/static/images/" + city + "/" + fileName);
        Resource resource = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
