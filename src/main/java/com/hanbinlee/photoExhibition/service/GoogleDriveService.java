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
                .setFields("nextPageToken, files(id, name, mimeType)")
                .execute();

        return result.getFiles();
    }

    public List<File> listFolders(String folderId) throws IOException {
        FileList result = googleDrive().files().list()
                .setQ("'" + folderId + "' in parents and trashed = false")
                .setFields("nextPageToken, files(id, name, mimeType)")
                .execute();

        return result.getFiles();
    }

}

