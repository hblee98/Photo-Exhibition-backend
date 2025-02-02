package com.hanbinlee.photoExhibition.controller;

import com.hanbinlee.photoExhibition.entity.Photo;
import com.hanbinlee.photoExhibition.service.CityService;
import com.hanbinlee.photoExhibition.service.PhotoService;
import org.springframework.web.bind.annotation.*;
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


}
