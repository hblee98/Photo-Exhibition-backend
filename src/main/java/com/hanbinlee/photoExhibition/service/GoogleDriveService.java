package com.hanbinlee.photoExhibition.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Configuration
public class GoogleDriveService {
    @Bean
    public Drive googleDrive() throws IOException {
        InputStream credentialsStream = getClass().getResourceAsStream("/credentials.json");
        if (credentialsStream == null) {
            throw new IOException("Google credentials file not found.");
        }
        GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(
                credential.getTransport(),
                credential.getJsonFactory(),
                credential)
                .setApplicationName("Photo Exhibition")
                .build();
    }
    public List<File> listFilesInFolder(String folderId) throws IOException {
        FileList result = googleDrive().files().list()
                .setQ("'" + folderId + "' in parents and trashed = false")
                .setFields("files(id, name)")
                .execute();
        return result.getFiles();
    }
    public List<File> listFolders(String folderId) throws IOException {
        FileList result = googleDrive().files().list()
                .setQ("'" + folderId + "' in parents and trashed = false")
                .setFields("files(id, name)")
                .execute();
        return result.getFiles();
    }
    public void downloadAndSaveFile(String folderName, String fileId, String fileName) throws IOException {
        String folderPath = "src/main/resources/static/images/" + folderName + "/";
        Files.createDirectories(Paths.get(folderPath));
        java.io.File localFile = new java.io.File(folderPath + fileName);
        if (localFile.exists()) {
            System.out.println("The file already exists: " + fileName);
            return;
        }
        InputStream inputStream = googleDrive().files().get(fileId).executeMediaAsInputStream();
        Files.copy(inputStream, Paths.get(localFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Download completed: " + folderPath + fileName);
    }
}