package com.ide.api.service;

import com.ide.api.configurations.FilePaths;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class FileService {

    String basePath = FilePaths.BASE_PATH;
    String slashVsAntiSlash = FilePaths.SLASH_VS_ANTI_SLASH;
    public void storeFile(String nomFichier,
                          String dossierFichier,
                          String base64){
        String path = basePath + dossierFichier;
        File nFichier = new File(nomFichier);
        String locFichier = new File(path).getAbsolutePath()+ slashVsAntiSlash+ nFichier;
        try(FileOutputStream fileOutputStream = new FileOutputStream(locFichier);) {
            byte[] decoder = Base64.getDecoder().decode(base64);
            fileOutputStream.write(decoder);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String getFile(String nomFichier) throws NoSuchAlgorithmException, InvalidKeyException {
        String base64Fichier = encoderFichierToBase64(nomFichier);
        return base64Fichier;
    }

    public byte[] getByteFile(String fileId) {
        try {
            Path filePath = Paths.get(basePath).resolve(fileId);
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Echec de lecture de fichier:  " + fileId, ex);
        }
    }

    public static String encoderFichierToBase64(String path){
        File fichier = new File(path);
        try{
            byte[] contenuFichier = Files.readAllBytes(fichier.toPath());
            return Base64.getEncoder().encodeToString(contenuFichier);
        } catch (Exception ex){
            throw new IllegalStateException("Impossible de lire le fichier" + fichier, ex);
        }
    }
}
