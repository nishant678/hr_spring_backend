package com.hr.demo.service.faceverify;

import org.springframework.web.multipart.MultipartFile;

/**
 * Client for the Python FastAPI face-verification microservice.
 */
public interface FaceVerificationClient {

    boolean isEnabled();

    /**
     * Verify a check-in face photo against the employee's registered embedding.
     *
     * @throws FaceVerificationException if the service rejects the request or is unreachable.
     */
    VerifyOutcome verify(String employeeId, MultipartFile faceImage);

    /** Register (or update) the employee's face embedding. */
    void register(String employeeId, String name, MultipartFile faceImage);

    /** Whether a face embedding exists for the employee. */
    boolean isRegistered(String employeeId);

    record VerifyOutcome(boolean matched, double score, double threshold) {}
}
