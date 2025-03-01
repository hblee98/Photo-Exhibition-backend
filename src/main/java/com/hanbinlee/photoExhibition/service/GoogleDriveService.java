package com.hanbinlee.photoExhibition.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.auth.http.HttpCredentialsAdapter;
import java.security.GeneralSecurityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import net.coobird.thumbnailator.Thumbnails;



@Configuration
public class GoogleDriveService {
    @Bean
    public Drive googleDrive() throws IOException, GeneralSecurityException {
        InputStream credentialsStream = getClass().getResourceAsStream("/credentials.json");
        if (credentialsStream == null) {
            throw new IOException("Google credentials file not found.");
        }
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        
        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer)
                .setApplicationName("Photo Exhibition")
                .build();
    }
    public List<File> listFilesInFolder(String folderId) throws IOException, GeneralSecurityException {
        FileList result = googleDrive().files().list()
                .setQ("'" + folderId + "' in parents and trashed = false")
                .setFields("files(id, name)")
                .execute();
        return result.getFiles();
    }
    public List<File> listFolders(String folderId) throws IOException, GeneralSecurityException {
        FileList result = googleDrive().files().list()
                .setQ("'" + folderId + "' in parents and trashed = false")
                .setFields("files(id, name)")
                .execute();
        return result.getFiles();
    }
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    public void downloadAndSaveFile(String folderName, String fileId, String fileName) throws IOException, GeneralSecurityException {
        String sanitizedFileName = sanitizeFileName(fileName);
        String folderPath = "src/main/resources/static/images/" + folderName + "/";
        String thumbnailFolderPath = "src/main/resources/static/images/" + folderName + "/thumbnail/";

        Files.createDirectories(Paths.get(folderPath));
        Files.createDirectories(Paths.get(thumbnailFolderPath));

        java.io.File originalFile = new java.io.File(folderPath + sanitizedFileName);
        java.io.File thumbnailFile = new java.io.File(thumbnailFolderPath + sanitizedFileName);

        if (originalFile.exists() && thumbnailFile.exists()) {
            System.out.println("The file already exists: " + sanitizedFileName);
            return;
        }

        InputStream inputStream = googleDrive().files().get(fileId).executeMediaAsInputStream();
        Files.copy(inputStream, Paths.get(originalFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Download completed: " + folderPath + sanitizedFileName);

        try {
            BufferedImage image = ImageIO.read(originalFile);
            if (image != null) {
                Thumbnails.of(originalFile)
                        .size(600, 600)
                        .keepAspectRatio(true)
                        .useExifOrientation(true)
                        .outputFormat("jpg")
                        .outputQuality(0.8)
                        .toFile(thumbnailFile);

                System.out.println("Thumbnail saved: " + thumbnailFile.getAbsolutePath());
            } else {
                System.err.println("Failed to read image: " + originalFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create thumbnail: " + e.getMessage());
        }
    }
}