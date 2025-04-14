package com.hanbinlee.photoExhibition.service;

import com.hanbinlee.photoExhibition.entity.City;
import com.hanbinlee.photoExhibition.entity.Photo;
import com.hanbinlee.photoExhibition.repository.PhotoRepository;
import com.hanbinlee.photoExhibition.repository.CityRepository;
import com.google.api.services.drive.model.File;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.GeneralSecurityException;
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
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Failed to download pictures: " + e.getMessage());
        }
    }
    public static int[] getImageDimensions(String cityName, String fileName) throws IOException {
        String filePath = "src/main/resources/static/images/" + cityName + "/" + fileName;
        java.io.File imageFile = new java.io.File(filePath);
        if (!imageFile.exists()) {
            System.err.println("File does not exist: " + filePath);
            return new int[]{0, 0};
        }
        if (!imageFile.canRead()) {
            System.err.println("Cannot read file: " + filePath);
            return new int[]{0, 0};
        }
        System.out.println("File path: " + imageFile.getAbsolutePath());
        System.out.println("File size: " + imageFile.length());
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            System.err.println("Failed to read image: format may not be supported");
            return new int[]{0, 0};
        }
        return new int[]{image.getWidth(), image.getHeight()};
    }
    public void savePhotoInfo() throws IOException, GeneralSecurityException {

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
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    private void savePhotoToCityTable(City city, File file) throws IOException {
        Photo photo = new Photo();
        photo.setCity(city);

        String[] parts = file.getName().split(" ", 2);
        String fileName = file.getName();
        String sanitizedFileName = sanitizeFileName(fileName);
        String fileNameWithoutExtension = parts[1].replaceAll("\\.[^\\.]+$", "");
        String cityName = city.getName();

        String originFilePath = "/api/photos/images/" + cityName + "/" + sanitizedFileName;
        String thumbnailFilePath = "/api/photos/images/" + cityName + "/thumbnail/" + sanitizedFileName;

        photo.setRegion(cityName);
        photo.setSubRegion(parts[0]);
        photo.setDescription(fileNameWithoutExtension);
        photo.setOriginFilePath(originFilePath);
        photo.setThumbnailFilePath(thumbnailFilePath);

        System.out.println("The Region: " + photo.getRegion());
        System.out.println("The Subregion: " + photo.getSubRegion());
        System.out.println("The description: " + photo.getDescription());
        System.out.println("The origin File Path: " + photo.getOriginFilePath());
        System.out.println("The thumbnail File Path: " + photo.getThumbnailFilePath());

        if (!photoRepository.existsByCityAndDescription(city, fileNameWithoutExtension)) {
            try {
                int[] dimensions = getImageDimensions(cityName, sanitizedFileName);
                photo.setWidth(dimensions[0]);
                photo.setHeight(dimensions[1]);
                System.out.println("Width: " + dimensions[0] + ", Height: " + dimensions[1]);
            } catch (IOException e) {
                System.err.println("Failed to get image dimensions: " + e.getMessage());
                e.printStackTrace();
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
    public void deletePhoto(Long id) throws IOException, GeneralSecurityException {
        try {
            Photo photo = photoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Photo not found"));

            System.out.println("Deleting photo with ID: " + id);
            System.out.println("Original file path: " + photo.getOriginFilePath());
            System.out.println("City name: " + photo.getCity().getName());

            String originFilePath = photo.getOriginFilePath();
            System.out.println("Original file path length: " + originFilePath.length());
            System.out.println("Last '/' position: " + originFilePath.lastIndexOf("/"));
            
            String fileName = originFilePath.substring(originFilePath.lastIndexOf("/") + 1);
            System.out.println("Extracted file name: '" + fileName + "'");
            System.out.println("File name length: " + fileName.length());

            String originalFileName = fileName.replaceAll("_", " ");
            System.out.println("Original file name (with spaces): '" + originalFileName + "'");
            System.out.println("Original file name length: " + originalFileName.length());

            String cityName = photo.getCity().getName();
            System.out.println("City name for deletion: '" + cityName + "'");
            System.out.println("City name length: " + cityName.length());


            String basePath = "src/main/resources/static/images/";
            java.io.File localFile = new java.io.File(basePath + cityName + "/" + fileName);
            java.io.File thumbnailFile = new java.io.File(basePath + cityName + "/thumbnail/" + fileName);
            
            System.out.println("Attempting to delete local file: " + localFile.getAbsolutePath());
            System.out.println("Attempting to delete thumbnail file: " + thumbnailFile.getAbsolutePath());
            
            boolean localFileDeleted = false;
            if (localFile.exists()) {
                if (!localFile.delete()) {
                    System.err.println("Failed to delete local file: " + localFile.getAbsolutePath());
                } else {
                    System.out.println("Successfully deleted local file");
                    localFileDeleted = true;
                }
            } else {
                System.out.println("Local file does not exist");
            }
            
            boolean thumbnailDeleted = false;
            if (thumbnailFile.exists()) {
                if (!thumbnailFile.delete()) {
                    System.err.println("Failed to delete thumbnail file: " + thumbnailFile.getAbsolutePath());
                } else {
                    System.out.println("Successfully deleted thumbnail file");
                    thumbnailDeleted = true;
                }
            } else {
                System.out.println("Thumbnail file does not exist");
            }

            photoRepository.delete(photo);
            System.out.println("Photo deleted successfully from database: ID=" + id);

            System.out.println("Deletion Summary:");
            System.out.println("- Local File: " + (localFileDeleted ? "Success" : "Failed"));
            System.out.println("- Thumbnail: " + (thumbnailDeleted ? "Success" : "Failed"));
            System.out.println("- Database: Success");

        } catch (Exception e) {
            System.err.println("Error in deletePhoto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    public Photo updatePhoto(Long id, Photo updatedPhoto) {
        Photo existingPhoto = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        existingPhoto.setDescription(updatedPhoto.getDescription());
        existingPhoto.setRegion(updatedPhoto.getRegion());
        
        return photoRepository.save(existingPhoto);
    }
}