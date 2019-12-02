package com.example.uploadfile.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Oleg Pavlyukov
 * on 28.11.2019
 * cpabox777@gmail.com
 */

@Service
public class FileStorage {
    private final Path rootDir = Paths.get("uploadDir");

    public Path store(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), rootDir.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("MultipartFile stream exception");
        }
        return rootDir.resolve(file.getOriginalFilename());
    }

    public Resource getFile(String filename) {
        Path file = rootDir.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Resource is not exist or readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Resource creation ex. Msg: " + e.getMessage());
        }

    }

    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootDir);
        } catch (IOException e) {
            throw new RuntimeException("Deletion files ex");
        }
    }

    public void init() {
        try {
            Files.createDirectory(rootDir);
        } catch (IOException e) {
            throw new RuntimeException("Dir creation ex");
        }
    }
}
