package com.recupera.item.back.recupera.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

@Service
public class GoogleDriveConfig {

    private static final String APPLICATION_NAME = "Recupera Item";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    // Diretório para armazenar os tokens de acesso e de atualização dos usuários.
    // Em produção, use um armazenamento seguro como um banco de dados.
    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File("credentials");
    //private static final String CLIENT_SECRETS_FILE_PATH = "/client_secrets.json.json";
    private static final String CLIENT_SECRETS_FILE_PATH = "/etc/secrets/client_secrets.json.json";

    private static final HttpTransport HTTP_TRANSPORT;
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível inicializar o HTTP Transport", e);
        }
    }

    /**
     * Cria um objeto Drive autorizado usando o fluxo OAuth 2.0.
     * Na primeira execução, abrirá o navegador para que o usuário autorize o acesso.
     * Nas execuções seguintes, usará o refresh_token armazenado.
     * @return Uma instância de Drive autorizada.
     * @throws Exception
     */
    private Credential getCredentials() throws Exception {
        InputStream in = GoogleDriveConfig.class.getResourceAsStream(CLIENT_SECRETS_FILE_PATH);
        if (in == null) {
            throw new IllegalStateException("Arquivo de credenciais não encontrado: " + CLIENT_SECRETS_FILE_PATH);
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Constrói o fluxo de autorização e o dispara.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, Collections.singleton(DriveScopes.DRIVE))
                .setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                .setAccessType("offline") // Garante que você receberá um refresh_token
                .build();
        
        // O "user" aqui é um identificador para o token armazenado.
        // Em uma aplicação web real, você usaria o ID do usuário logado.
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8088).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Faz o upload de um arquivo para o Google Drive.
     * @param multipartFile O arquivo a ser enviado.
     * @param folderId O ID da pasta no Drive onde o arquivo será salvo (opcional).
     * @return O link de visualização do arquivo no Drive.
     * @throws Exception
     */
    public String uploadFile(MultipartFile multipartFile, String folderId) throws Exception {
        Credential credential = getCredentials();
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());
        if (folderId != null && !folderId.isEmpty()) {
            fileMetadata.setParents(Collections.singletonList(folderId));
        }

        InputStreamContent mediaContent = new InputStreamContent(
            multipartFile.getContentType(),
            multipartFile.getInputStream()
        );

        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute();

        return file.getWebViewLink();
    }
}
