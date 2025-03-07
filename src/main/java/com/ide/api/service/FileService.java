package com.ide.api.service;

import com.ide.api.configurations.FilePaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
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
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String getFile(String nomFichier){
        return encoderFichierToBase64(nomFichier);
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


    public boolean isH264Video(String filePath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=codec_name",
                "-of", "default=nw=1:nk=1",
                filePath
        );

        Process process = processBuilder.start();
        try (InputStream inputStream = process.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String codecName = reader.readLine();
            return "h264".equals(codecName);
        }
    }



    public void reencodeVideoToH264(String inputFilePath, String outputFilePath) throws IOException {
        logger.info("Entrer dans la fonction reencodage!");
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", inputFilePath,
                "-vcodec", "libx264",
                "-preset", "fast",
                "-crf", "23",
                "-vf", "scale='min(1280,iw)':-2",
                "-tune", "film",
                "-acodec", "aac",
                "-b:a", "128k",
                "-movflags", "+faststart",
                "-strict", "experimental",
                outputFilePath
        );

        // Rediriger les sorties de processus
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (InputStream inputStream = process.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
        }


        try {
            logger.info("Entrer dans le try!");
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Méthode pour stocker un fichier en fonction de son type (réencodage vidéo uniquement si vidéo)
    public void storeFileWithReencoding(String nomFichier, String dossierFichier, String base64) throws IOException {
        String fileName = nomFichier;
        if (isVideoFile(fileName)) {
            String tempFilePath = basePath + dossierFichier + File.separator + fileName;
            storeFile(fileName, dossierFichier, base64);
            logger.info("InputPath: {}", tempFilePath );
            if (!isH264Video(tempFilePath)){
                String outputFilePath = basePath + dossierFichier + File.separator + "LIBEIL_" + fileName;
                reencodeVideoToH264(tempFilePath, outputFilePath);
                logger.info("OutputPath: {}", outputFilePath );
                new File(tempFilePath).delete();
            }else{
                new File(tempFilePath).renameTo(new File(basePath + dossierFichier + File.separator + "LIBEIL_" + fileName));
            }
        } else {
            // Si ce n'est pas une vidéo, on peut simplement stocker le fichier sans réencodage
            storeFile(fileName, dossierFichier, base64);
        }
    }

}
