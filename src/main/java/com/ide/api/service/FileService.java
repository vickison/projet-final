package com.ide.api.service;

import com.ide.api.configurations.FilePaths;
import com.ide.api.configurations.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Set;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    String basePath = FilePaths.BASE_PATH;
    String slashVsAntiSlash = FilePaths.SLASH_VS_ANTI_SLASH;

    private static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "avi", "mkv", "mov", "flv", "webm");
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

    // Méthode pour obtenir l'extension d'un fichier à partir de son nom
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex > 0) ? fileName.substring(lastDotIndex + 1) : "";
    }

    // Méthode pour vérifier si le fichier est une vidéo en fonction de son extension
    private boolean isVideoFile(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return VIDEO_EXTENSIONS.contains(extension);
    }



    public void reencodeVideoToH264(String inputFilePath, String outputFilePath) throws IOException {
        logger.info("Entrer dans la fonction reencodage!");
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", inputFilePath,
                "-vcodec", "libx264",
                "-preset", "fast",  // Choisir un preset pour une meilleure performance
                "-acodec", "aac",
                "-b:a", "128k",     // Débit audio
                "-strict", "experimental", // Si nécessaire pour utiliser certains codecs expérimentaux
                outputFilePath
        );

        // Rediriger les sorties de processus
        processBuilder.redirectErrorStream(true); // Combinaison des flux stdout et stderr
        Process process = processBuilder.start();
        try (InputStream inputStream = process.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line); // Logger chaque ligne de sortie
            }
        }


        try {
            logger.info("Entrer dans le try!");
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting reencode video", e);
        }
    }

    // Méthode pour stocker un fichier en fonction de son type (réencodage vidéo uniquement si vidéo)
    public void storeFileWithReencoding(String nomFichier, String dossierFichier, String base64) throws IOException {
        // Si le fichier est une vidéo, on doit la réencoder
        String fileName = nomFichier;
        if (isVideoFile(fileName)) {
            String tempFilePath = basePath + dossierFichier + File.separator + fileName;
            storeFile(fileName, dossierFichier, base64);
            logger.info("InputPath: {}", tempFilePath );
            // Réencoder la vidéo
            String outputFilePath = basePath + dossierFichier + File.separator + "LIBEIL_" + fileName;
            reencodeVideoToH264(tempFilePath, outputFilePath);
            logger.info("OutputPath: {}", outputFilePath );
            // Supprimer le fichier temporaire original après réencodage
            new File(tempFilePath).delete();
        } else {
            // Si ce n'est pas une vidéo, on peut simplement stocker le fichier sans réencodage
            storeFile(fileName, dossierFichier, base64);
        }
    }

}
