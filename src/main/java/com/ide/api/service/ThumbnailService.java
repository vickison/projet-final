package com.ide.api.service;

import com.ide.api.entities.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ThumbnailService {

    private final String thumbnailBasePath = "C:\\Users\\avicky\\libeil\\thumbnail\\";


    public byte[] generateThumbnail(byte[] fileData, Integer fileId, String fileName, int thumbnailWidth, int thumbnailHeight) throws IOException {
        //byte[] fileData = documentService.getDocumentData(fileId);
        //Optional<Document> documentOptional = documentService.findDocument(fileId);
        String fileExtension = getFileExtension(fileName);
        String thumbnailFileName = fileId+ "-thumbnail.jpg";

        if (isImageFile(fileExtension)) {
            return generateImageThumbnail(fileData, thumbnailWidth, thumbnailHeight, thumbnailFileName);
        } else if (fileExtension.equalsIgnoreCase("pdf")) {
            return generatePdfThumbnail(fileData, thumbnailWidth, thumbnailHeight, thumbnailFileName);
        } else if (isVideoFile(fileExtension)) {
            return generateVideoThumbnail(fileData, thumbnailWidth, thumbnailHeight, thumbnailFileName);
        } else {
            return generateDefaultThumbnail(thumbnailWidth, thumbnailHeight, thumbnailFileName);
        }
    }

    private boolean isImageFile(String fileExtension) {
        return fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg") ||
                fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("gif") ||
                fileExtension.equalsIgnoreCase("bmp") || fileExtension.equalsIgnoreCase("tiff");
    }

    private boolean isVideoFile(String fileExtension) {
        return fileExtension.equalsIgnoreCase("mp4") || fileExtension.equalsIgnoreCase("avi") ||
                fileExtension.equalsIgnoreCase("mkv") || fileExtension.equalsIgnoreCase("mov") ||
                fileExtension.equalsIgnoreCase("wmv") || fileExtension.equalsIgnoreCase("flv");
    }

    private String getFileExtension(String fileId) {
        return fileId.substring(fileId.lastIndexOf(".") + 1).toLowerCase();
    }

    private byte[] generateImageThumbnail(byte[] imageData, int thumbnailWidth, int thumbnailHeight, String thumbnailFileName) throws IOException {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));
        BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
        thumbnail.createGraphics().drawImage(img.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH), 0, 0, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "jpg", baos);

        saveThumbnailToFileSystem(baos.toByteArray(), thumbnailFileName);

        return baos.toByteArray();
    }

    public void saveThumbnailToFileSystem2(byte[] thumbnailData, String thumbnailFileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(thumbnailFileName)) {
            fos.write(thumbnailData);
        }
    }

    private void saveThumbnailToFileSystem(byte[] thumbnailData, String thumbnailFileName) throws IOException {
        Path thumbnailPath = Paths.get(thumbnailBasePath + thumbnailFileName);
        Files.write(thumbnailPath, thumbnailData);
    }

    public String saveThumbnailToFileSystemStr(byte[] thumbnailData, Integer fileId) throws IOException {
        // Sauvegarder la miniature sur le système de fichiers
        String thumbnailFileName = fileId + "-thumbnail.jpg";
        String thumbnailFilePath = thumbnailBasePath + thumbnailFileName;
        Files.write(Paths.get(thumbnailFilePath), thumbnailData);
        return thumbnailFilePath;
    }

    private byte[] generatePdfThumbnail(byte[] pdfData, int thumbnailWidth, int thumbnailHeight, String thumbnailFileName) throws IOException {
        try (PDDocument document = PDDocument.load(pdfData)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            PDPage firstPage = document.getPage(0); // Récupérer la première page du PDF

            // Rendre la première page en tant qu'image BufferedImage avec une haute résolution
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 400, ImageType.RGB); // Augmenter DPI à 400 pour meilleure qualité

            // Redimensionner l'image à la taille de la miniature avec une qualité élevée
            BufferedImage thumbnail = resizeImage(bufferedImage, thumbnailWidth, thumbnailHeight);

            // Convertir BufferedImage en tableau de bytes
            byte[] thumbnailData = bufferedImageToBytes(thumbnail, "png"); // Utiliser PNG pour meilleure qualité

            // Enregistrer la miniature dans le système de fichiers
            saveThumbnailToFileSystem(thumbnailData, thumbnailFileName);

            return thumbnailData;
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        // Utiliser un algorithme de redimensionnement de haute qualité
        g.drawImage(originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();
        return resizedImage;
    }

    private byte[] bufferedImageToBytes(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }
    }
    private byte[] generateVideoThumbnail(byte[] videoData, int thumbnailWidth, int thumbnailHeight, String thumbnailFileName) throws IOException {
        // Sauvegarder temporairement la vidéo
        String videoFilePath = saveVideoTemporarily(videoData);

        // Générer la miniature avec FFmpeg
        String thumbnailPath = thumbnailBasePath + thumbnailFileName;
        generateVideoThumbnailWithFFmpeg(videoFilePath, thumbnailPath, thumbnailWidth, thumbnailHeight);

        // Lire la miniature depuis le fichier système
        byte[] thumbnailData = Files.readAllBytes(Paths.get(thumbnailPath));

        // Supprimer les fichiers temporaires
        Files.deleteIfExists(Paths.get(videoFilePath));
        Files.deleteIfExists(Paths.get(thumbnailPath));

        return thumbnailData;
    }

    private String saveVideoTemporarily(byte[] videoData) throws IOException {
        String tempVideoPath = thumbnailBasePath + "video\\temp_video.mp4"; // Chemin où sauvegarder la vidéo temporairement
        Files.write(Paths.get(tempVideoPath), videoData);
        return tempVideoPath;
    }

    private void generateVideoThumbnailWithFFmpeg(String videoFilePath, String thumbnailPath, int thumbnailWidth, int thumbnailHeight) throws IOException {
        // Commande FFmpeg pour générer la miniature
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg",
                "-i", videoFilePath,
                "-ss", "00:00:01", // Extrait l'image à la 1ère seconde de la vidéo (ajustez selon vos besoins)
                "-vframes", "1", // Nombre de frames à extraire
                "-s", thumbnailWidth + "x" + thumbnailHeight, // Dimensions de la miniature
                "-f", "image2",
                thumbnailPath
        );

        Process process = processBuilder.start();
        try {
            process.waitFor(); // Attendre la fin de la commande FFmpeg
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for video thumbnail generation", e);
        }
    }

    private byte[] generateDefaultThumbnail(int thumbnailWidth, int thumbnailHeight, String thumbnailFileName) throws IOException {
        BufferedImage image = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, thumbnailWidth, thumbnailHeight);
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        saveThumbnailToFileSystem(baos.toByteArray(), thumbnailFileName);

        return baos.toByteArray();
    }
}
