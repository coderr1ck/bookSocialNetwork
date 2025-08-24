package com.coderrr1ck.bookBackend.service;

import jakarta.mail.Multipart;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
public class FileService {
    @Value("${application.file.uploads.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(@NotNull MultipartFile file, @NotNull UUID userId){
//        how will you save file on server ,
//        create a folder for each user and
//        uniquely identify each book cover by its id
        final String fileUploadSubPath = "users" + File.separator  + userId;
        return uploadFile(file,fileUploadSubPath);
    }

    private String uploadFile(@NotNull MultipartFile sourceFile,
                              @NotNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated){
                log.warn("Failed to create file path with folders");
                return null;
            }
            log.info("Target Folder created : " + folderCreated);
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath,sourceFile.getBytes());
            log.info("File saved to  :"+targetPath);
            return targetFilePath;
        }catch (IOException e){
            log.error("File was not saved"+e.getMessage());
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.isEmpty()){
            log.warn("Orignal file name is empty.");
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if(lastDotIndex == -1){
            log.warn("File does not have an extension.");
            return "";
        }
        return originalFilename.substring(lastDotIndex).toLowerCase();
    }

}
