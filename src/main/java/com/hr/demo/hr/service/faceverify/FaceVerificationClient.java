package com.hr.demo.hr.service.faceverify;

import org.springframework.web.multipart.MultipartFile;

/**
 * Client for the Python FastAPI face-verification microservice.
 */
public interface FaceVerificationClient {

    boolean isEnabled();

    VerifyOutcome verify(String employeeId, MultipartFile faceImage);
    void register(String employeeId, String name, MultipartFile faceImage);
    boolean isRegistered(String employeeId);

    FaceProcessResponse processRegistration(MultipartFile image);
    FaceProcessResponse processVerify(MultipartFile image);

    record VerifyOutcome(boolean matched, double score, double threshold) {}
}
