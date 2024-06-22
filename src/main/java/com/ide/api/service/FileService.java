package com.ide.api.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class FileService {
    private final String basePath ="C:\\Users\\avicky\\libeil\\";

    public void storeFile(String nomFichier,
                          String dossierFichier,
                          String base64){
        String path = basePath + dossierFichier;
        File nFichier = new File(nomFichier);
        String locFichier = new File(path).getAbsolutePath()+ "\\"+ nFichier;
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
