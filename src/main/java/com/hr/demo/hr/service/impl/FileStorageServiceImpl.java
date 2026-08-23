package com.hr.demo.hr.service.impl;

import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.hr.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path uploadPath;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp",
            "pdf", "doc", "docx", "xls", "xlsx", "txt",
            "mp4", "avi", "mov", "mkv", "webm"
    );

    @PostConstruct
    public void init() {
        uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + uploadPath, e);
        }
    }

    @Override
    public String storeFile(MultipartFile file, Long companyId, Long leaveId) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.isBlank()) {
            throw new BadRequestException("File name is empty");
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("File type not allowed: " + extension);
        }

        String storedFilename = UUID.randomUUID() + "." + extension;
        Path companyDir = uploadPath.resolve("leaves").resolve(String.valueOf(companyId));
        try {
            Files.createDirectories(companyDir);
            Path targetPath = companyDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "leaves/" + companyId + "/" + storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + storedFilename, e);
        }
    }

    @Override
    public String storeProfilePhoto(MultipartFile file, Long companyId) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.isBlank()) {
            throw new BadRequestException("File name is empty");
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("File type not allowed: " + extension);
        }

        String storedFilename = UUID.randomUUID() + "." + extension;
        Path companyDir = uploadPath.resolve("profiles").resolve(String.valueOf(companyId));
        try {
            Files.createDirectories(companyDir);
            Path targetPath = companyDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "profiles/" + companyId + "/" + storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + storedFilename, e);
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path filePath = uploadPath.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            throw new RuntimeException("File not found: " + filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }

    @Override
    public String getFileUrl(String filename) {
        return "/api/files/" + filename;
    }
}
