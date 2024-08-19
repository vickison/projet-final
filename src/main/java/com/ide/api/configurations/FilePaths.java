package com.ide.api.configurations;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class FilePaths {

    private static final String BASE_DIR = "libeil";

    // Définissez les chemins de fichiers ici
    public static final String BASE_PATH = "/libeilBack-End/LibEIlH/";
    //public static final String BASE_PATH = "C:\\Users\\avicky\\libeil\\";


    public static final String THUMBNAIL_BASE_PATH = BASE_PATH + "thumbnail/";
    //public static final String THUMBNAIL_BASE_PATH = BASE_PATH + "thumbnail\\";


    public static final String THUMBNAIL_VIDEO_TEMP_LOC = THUMBNAIL_BASE_PATH + "video/temp_video.mp4";
    //public static final String THUMBNAIL_VIDEO_TEMP_LOC = THUMBNAIL_BASE_PATH + "video\\temp_video.mp4";

    public static final Path THUMBNAIL_LOCATION = Paths.get(THUMBNAIL_BASE_PATH);

    public static final String SLASH_VS_ANTI_SLASH = "/";
    //public static final String SLASH_VS_ANTI_SLASH = "\\";




    //public static final String THUMBNAIL_VIDEO_TEMP_LOC = THUMBNAIL_BASE_PATH + "video\\temp_video.mp4";
    //private final String basePath ="C:\\Users\\avicky\\libeil\\";
    //private final String thumbnailBasePath = "C:\\Users\\avicky\\libeil\\thumbnail\\";
    //private final Path thumbnailLocation = Paths.get("C:\\Users\\avicky\\libeil\\thumbnail\\");
    private FilePaths() {
        throw new UnsupportedOperationException("Cette classe ne peut pas être instanciée");
    }
}

