package com.coderrr1ck.bookBackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileUtils {
    public static byte[] readFileFromLocation(String bookCover, UUID bookId) {
        if(bookCover == null || bookCover.isEmpty()){
            return new byte[]{};
        }
        Path path = Paths.get(bookCover);
        if(Files.exists(path)){
            try {
                byte[] context = Files.readAllBytes(path);
                return context;
            }catch (Exception e){
                log.error("File not found for book : "+bookId);
                return null;
            }
        }
        return null;
    }
}
