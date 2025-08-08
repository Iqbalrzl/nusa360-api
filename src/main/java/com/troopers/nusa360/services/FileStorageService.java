package com.troopers.nusa360.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path publicDirectoryPath;
    private final Path privateDirectoryPath;

    public FileStorageService(
            @Value("${spring.file.upload.public}") String publicDir,
            @Value("${spring.file.upload.private}") String privateDir
    ){
        this.publicDirectoryPath = Paths.get(publicDir).toAbsolutePath().normalize();
        this.privateDirectoryPath = Paths.get(privateDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.publicDirectoryPath);
            Files.createDirectories(this.privateDirectoryPath);;
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directories", e);
        }
    }

    public String storeUserFilePublic(MultipartFile file, Long userId) {
        return storeUserFile(file, userId, true);
    };

    public String storeUserFilePrivate(MultipartFile file, Long userId) {
        return storeUserFile(file, userId, false);
    }

    private String storeUserFile(MultipartFile file, Long userId, Boolean publicAccess) {
        try {
            String uniqueID = UUID.randomUUID().toString();
            String fileCategory = getFileCategory(file);


            Path baseDir = publicAccess ? publicDirectoryPath : privateDirectoryPath;

            Path userDirectory = baseDir.resolve(Paths.get(
                    String.valueOf(userId)
            )).normalize();

            Files.createDirectories(userDirectory);

            String fileName = uniqueID + file.getOriginalFilename().replaceAll("\\s+", "");
            Path destination = userDirectory.resolve(fileName).normalize();

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return baseDir.relativize(destination).toString().replace("\\", "/");

        } catch (IOException e){
            throw new RuntimeException("Could not store file", e);
        }
    }

    private String getFileExtension(String fileName) {
        return fileName != null && fileName.contains(".") ?
                fileName.substring(fileName.lastIndexOf("."))
                : "";
    }

    private String getFileCategory(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image")) return "images";
            else if (contentType.startsWith("video")) return "videos";
            else if (contentType.startsWith("text") || contentType.startsWith("application")) return "documents";
        }
        return "others";
    }
}
