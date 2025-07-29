package com.recupera.item.back.recupera.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // <-- Mude para Configuration

@Configuration // <-- Anotação correta para uma classe de configuração
public class GoogleDriveConfig {
    
    private static final String APPLICATION_NAME = "Recupera Item";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    // Garanta que este caminho corresponde ao seu Secret File na Render
    private static final String SERVICE_ACCOUNT_KEY_PATH = "/etc/secrets/webtrab-3db415c0495a.json";

    @Bean
    public Drive driveService() throws GeneralSecurityException, IOException {
        InputStream in = new FileInputStream(SERVICE_ACCOUNT_KEY_PATH);
        
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new Drive.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}