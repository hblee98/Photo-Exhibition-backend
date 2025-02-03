package com.hanbinlee.photoExhibition.service;

import com.hanbinlee.photoExhibition.entity.City;
import com.hanbinlee.photoExhibition.entity.Photo;
import com.hanbinlee.photoExhibition.repository.PhotoRepository;
import com.hanbinlee.photoExhibition.repository.CityRepository;
import com.google.api.services.drive.model.File;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    @PostConstruct
    public void init(){
        try{
            List<File> cityFolders = googleDriveService.listFolders(photoFolderId);
            for(File folder : cityFolders){
                String folderName = folder.getName();
                String folderId = folder.getId();

                List<File> photos = googleDriveService.listFilesInFolder(folderId);
                for(File photo : photos) {
                    googleDriveService.downloadAndSaveFile(folderName, photo.getId(), photo.getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to download pictures: " + e.getMessage());
        }
    }
    public static int[] getImageDimensions(String cityName, String fileName) throws IOException {
        String filePath = "src/main/resources/static/images/" + cityName + "/" + fileName;
        java.io.File imageFile = new java.io.File(filePath);
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Unable to read image from file: " + filePath);
        }
        return new int[]{image.getWidth(), image.getHeight()};
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
    private void savePhotoToCityTable(City city, File file) throws IOException {
        Photo photo = new Photo();
        photo.setCity(city);

        String[] parts = file.getName().split(" ", 2);
        String fileNameWithoutExtension = parts[1].replaceAll("\\.[^\\.]+$", "");
        String cityName = city.getName();
        String filePath = "/api/photos/" + cityName + "/" + file.getName();

        photo.setRegion(cityName);
        photo.setSubRegion(parts[0]);
        photo.setDescription(fileNameWithoutExtension);
        photo.setFilePath(filePath);

        System.out.println("The Region: " + photo.getRegion());
        System.out.println("The Subregion: " + photo.getSubRegion());
        System.out.println("The description: " + photo.getDescription());
        System.out.println("File Path: " + photo.getFilePath());

        if (!photoRepository.existsByCityAndDescription(city, fileNameWithoutExtension)) {
            String fileName = file.getName();
            try {
                int[] dimensions = getImageDimensions(cityName, fileName);
                photo.setWidth(dimensions[0]);
                photo.setHeight(dimensions[1]);
                System.out.println("Width: " + dimensions[0] + ", Height: " + dimensions[1]);
            } catch (IOException e) {
                System.err.println("Failed to get image dimensions: " + e.getMessage());
                photo.setWidth(0);
                photo.setHeight(0);
            }
            photoRepository.save(photo);
            System.out.println("Photo saved to database.");
        } else {
            System.out.println("Photo already exists in the database for city: " + cityName + ", title: " + fileNameWithoutExtension);
        }
    }
    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }
    public List<Photo> getPhotosByRegion(String region) {
        return photoRepository.findByCityName(region);
    }
}