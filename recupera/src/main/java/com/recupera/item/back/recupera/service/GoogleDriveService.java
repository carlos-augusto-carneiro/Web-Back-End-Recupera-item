package com.recupera.item.back.recupera.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.google.api.services.drive.model.File;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

@Service
public class GoogleDriveService {

    private final Drive driveService;

    // Injeção de dependência: O Spring entrega o Bean 'Drive' que você criou na config
    @Autowired
    public GoogleDriveService(Drive driveService) {
        this.driveService = driveService;
    }

    public String uploadFile(MultipartFile multipartFile, String folderId) throws Exception {
        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());
        if (folderId != null && !folderId.isEmpty()) {
            fileMetadata.setParents(Collections.singletonList(folderId));
        }

        InputStreamContent mediaContent = new InputStreamContent(
            multipartFile.getContentType(),
            multipartFile.getInputStream()
        );

        // Agora 'this.driveService' existe e pode ser usado
        File file = this.driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute();

        return file.getWebViewLink();
    }
}