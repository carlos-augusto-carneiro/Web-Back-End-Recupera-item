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

@Service
public class GoogleDriveConfig {

    private static final String APPLICATION_NAME = "Recupera Item";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CLIENT_SECRETS_FILE_PATH = "/etc/secrets/webtrab-3db415c0495a.json";

    @Autowired
    private Drive driveService;

    /**
     * Cria um objeto Drive autorizado usando o fluxo OAuth 2.0.
     * Na primeira execução, abrirá o navegador para que o usuário autorize o acesso.
     * Nas execuções seguintes, usará o refresh_token armazenado.
     * @return Uma instância de Drive autorizada.
     * @throws Exception
     */
    /*
    private Credential getCredentials() throws Exception {
        // Carrega as credenciais do arquivo client_secrets.json.
        InputStream in = new FileInputStream(CLIENT_SECRETS_FILE_PATH);
        //InputStream in = GoogleDriveConfig.class.getResourceAsStream(CLIENT_SECRETS_FILE_PATH);
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
    } */

    @Bean
    public Drive driveService() throws GeneralSecurityException, IOException {
        // Carrega as credenciais da conta de serviço a partir do arquivo
        InputStream in = new FileInputStream(CLIENT_SECRETS_FILE_PATH);
        
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        // Inicializa o HttpTransport
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // Constrói e retorna o serviço do Drive, pronto para ser usado
        return new Drive.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Faz o upload de um arquivo para o Google Drive.
     * @param multipartFile O arquivo a ser enviado.
     * @param folderId O ID da pasta no Drive onde o arquivo será salvo (opcional).
     * @return O link de visualização do arquivo no Drive.
     * @throws Exception
     */
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

        File file = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id, webViewLink")
                .execute();

        return file.getWebViewLink();
    }
}
