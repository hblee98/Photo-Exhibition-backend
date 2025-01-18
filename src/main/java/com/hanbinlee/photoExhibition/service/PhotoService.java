package com.hanbinlee.photoExhibition.service;

import com.hanbinlee.photoExhibition.entity.City;
import com.hanbinlee.photoExhibition.entity.Photo;
import com.hanbinlee.photoExhibition.repository.PhotoRepository;
import com.hanbinlee.photoExhibition.repository.CityRepository;
import com.google.api.services.drive.model.File;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.util.List;

@Service
public class PhotoService {

    private final CityRepository cityRepository;
    private final PhotoRepository photoRepository;
    private final GoogleDriveService googleDriveService;

    @Value("${google.drive.photo-folder-id}")
    private String photoFolderId;

    public PhotoService(PhotoRepository photoRepository,CityRepository cityRepository,GoogleDriveService googleDriveService) {
        this.photoRepository = photoRepository;
        this.cityRepository = cityRepository;
        this.googleDriveService = googleDriveService;
    }
    public void savePhotoInfo() throws IOException {

        List<File> folders = googleDriveService.listFolders(photoFolderId);

        for (File folder : folders) {
            String folderName = folder.getName();
            String folderId = folder.getId();
            List<File> files = googleDriveService.listFilesInFolder(folderId);
            City city = cityRepository.findByName(folderName)
                    .orElseGet(() -> {
                        City newCity = new City();
                        newCity.setName(folderName);
                        return cityRepository.save(newCity);
                    });
            for (File file : files) {
                savePhotoToCityTable(city, file);
            }
        }
    }


    private void savePhotoToCityTable(City city, File file) {

        Photo photo = new Photo();
        photo.setCity(city);

        String[] parts = file.getName().split(" ",2);
        String fileNameWithoutExtension = parts[1].replaceAll("\\.[^\\.]+$", "");
        String cityName = city.getName();
        photo.setRegion(cityName);
        photo.setSubRegion(parts[0]);
        photo.setDescription(fileNameWithoutExtension);
        photo.setImageUrl("https://drive.google.com/uc?id=" + file.getId());

        System.out.println("The Region: " +photo.getCity());
        System.out.println("The Subregion :"+photo.getSubRegion());
        System.out.println("The description: " +photo.getDescription());
        System.out.println("The Image URL: " +photo.getImageUrl());

        if (!photoRepository.existsByImageUrl(photo.getImageUrl())) {
            photoRepository.save(photo);
            System.out.println("Photo saved to database.");
        } else {
            System.out.println("Photo already exists in the database.");
        }
        }
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }



}

