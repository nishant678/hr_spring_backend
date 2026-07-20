package com.hr.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String storeFile(MultipartFile file, Long companyId, Long leaveId);

    Resource loadFile(String filename);

    String getFileUrl(String filename);
}
