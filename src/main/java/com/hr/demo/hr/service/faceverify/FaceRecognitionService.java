package com.hr.demo.hr.service.faceverify;

import com.hr.demo.hr.entity.FaceProfileEntity;
import com.hr.demo.hr.repository.FaceProfileRepository;
import com.hr.demo.hr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaceRecognitionService {

    private final FaceVerificationClient pythonClient;
    private final FaceProfileRepository faceProfileRepository;
    private final UserRepository userRepository;

    private static final double QUALITY_THRESHOLD = 0.5;
    private static final double SIMILARITY_THRESHOLD = 0.50;
    private static final int EXPECTED_DIM = 512;

    @Transactional
    public FaceRegistrationResult register(String employeeId, String name, MultipartFile image) {
        log.info("Face registration STARTED for employeeId={}", employeeId);

        // Replace the current enrollment only after the new selfie passes validation.
        Optional<FaceProfileEntity> existing = faceProfileRepository.findActiveByEmployeeId(employeeId);

        FaceProcessResponse response;
        try {
            response = pythonClient.processRegistration(image);
        } catch (FaceVerificationException ex) {
            log.error("Python service error for employeeId={}: {}", employeeId, ex.getMessage());
            return FaceRegistrationResult.failed("SERVICE_UNAVAILABLE",
                    "Face verification service unavailable: " + ex.getMessage());
        }

        if (response == null) {
            return FaceRegistrationResult.failed("SERVICE_UNAVAILABLE", "Face service returned no result");
        }
        if (!response.success()) {
            log.warn("Python REJECTED registration for employeeId={}: code={}, message={}",
                    employeeId, response.code() != null ? response.code() : response.reason(), response.message());
            return FaceRegistrationResult.failed(response.code() != null ? response.code() : response.reason(), response.message());
        }

        // Validate embedding
        if (response.embedding() == null || response.embedding().isEmpty()) {
            log.error("Empty embedding returned for employeeId={}", employeeId);
            return FaceRegistrationResult.failed("INVALID_EMBEDDING", "Python returned empty embedding");
        }

        double[] embedding = new double[response.embedding().size()];
        for (int i = 0; i < response.embedding().size(); i++) {
            Double value = response.embedding().get(i);
            if (value == null || !Double.isFinite(value)) {
                return FaceRegistrationResult.failed("INVALID_EMBEDDING", "Face embedding contains invalid values");
            }
            embedding[i] = value;
        }

        if (embedding.length != EXPECTED_DIM) {
            log.error("Embedding dimension mismatch: {} != {} for employeeId={}", embedding.length, EXPECTED_DIM, employeeId);
            return FaceRegistrationResult.failed("INVALID_EMBEDDING",
                    "Embedding dimension " + embedding.length + " != " + EXPECTED_DIM);
        }
        if (cosineSimilarity(embedding, embedding) < 0.99) {
            return FaceRegistrationResult.failed("INVALID_EMBEDDING", "Face embedding is empty");
        }

        // Validate quality
        if (response.qualityScore() < QUALITY_THRESHOLD) {
            log.warn("Quality score {} below threshold {} for employeeId={}", response.qualityScore(), QUALITY_THRESHOLD, employeeId);
            return FaceRegistrationResult.failed("LOW_QUALITY", "Face quality score too low: " + response.qualityScore());
        }

        // Validate liveness
        if (!response.livenessPassed() || response.livenessScore() < 0.3) {
            log.warn("Liveness score {} too low for employeeId={}", response.livenessScore(), employeeId);
            return FaceRegistrationResult.failed("LIVENESS_FAILED", "Liveness check failed");
        }

        // Validate face count
        if (response.faceCount() != 1) {
            log.warn("Face count {} != 1 for employeeId={}", response.faceCount(), employeeId);
            return FaceRegistrationResult.failed("MULTIPLE_FACES", "Only one face allowed");
        }

        // All validations passed — save embedding
        FaceProfileEntity profile = existing.orElseGet(() -> FaceProfileEntity.builder()
                .employeeId(employeeId)
                .active(true).build());
        profile.setEmbedding(embedding);
        profile.setModelName(response.modelName() != null ? response.modelName() : "buffalo_l");
        profile.setModelVersion(response.modelVersion() != null ? response.modelVersion() : "v1");
        profile.setQualityScore(response.qualityScore());
        profile.setLivenessScore(response.livenessScore());
        faceProfileRepository.save(profile);

        // Update user's face registered flag
        updateFaceRegisteredFlag(employeeId, true);

        log.info("Face REGISTERED successfully for employeeId={}: quality={}, liveness={}, dim={}",
                employeeId, response.qualityScore(), response.livenessScore(), embedding.length);
        return FaceRegistrationResult.success(profile.getId(), response.qualityScore(), response.livenessScore());
    }

    public FaceVerificationResponse verify(String employeeId, MultipartFile image) {
        log.info("Face verification for employeeId={}", employeeId);

        Optional<FaceProfileEntity> stored = faceProfileRepository.findActiveByEmployeeId(employeeId);
        if (stored.isEmpty()) {
            return FaceVerificationResponse.failed("NO_REGISTERED_FACE", "No registered face found");
        }
        FaceProfileEntity registered = stored.get();

        FaceProcessResponse response;
        try {
            response = pythonClient.processVerify(image);
        } catch (FaceVerificationException ex) {
            log.error("Python error during verify: {}", ex.getMessage());
            return FaceVerificationResponse.failed("SERVICE_UNAVAILABLE", ex.getMessage());
        }

        if (response == null) {
            return FaceVerificationResponse.failed("SERVICE_UNAVAILABLE", "Face service returned no result");
        }
        if (!response.success()) {
            return FaceVerificationResponse.failed(response.code() != null ? response.code() : response.reason(), response.message());
        }
        if (response.faceCount() != 1 || !response.livenessPassed()
                || response.qualityScore() < QUALITY_THRESHOLD || response.livenessScore() < 0.3) {
            return FaceVerificationResponse.failed("FACE_QUALITY_FAILED", "Use one clear, uncovered face in good lighting");
        }

        List<Double> emb = response.embedding();
        if (emb == null || emb.size() != EXPECTED_DIM) {
            return FaceVerificationResponse.failed("INVALID_EMBEDDING", "Invalid probe embedding");
        }

        double[] probe = new double[emb.size()];
        for (int i = 0; i < emb.size(); i++) {
            if (emb.get(i) == null || !Double.isFinite(emb.get(i))) {
                return FaceVerificationResponse.failed("INVALID_EMBEDDING", "Invalid probe embedding");
            }
            probe[i] = emb.get(i);
        }

        double similarity = cosineSimilarity(probe, registered.getEmbedding());
        boolean matched = similarity >= SIMILARITY_THRESHOLD;

        return FaceVerificationResponse.of(matched, similarity, SIMILARITY_THRESHOLD,
                response.qualityScore(), response.livenessScore());
    }

    public boolean isRegistered(String employeeId) {
        return faceProfileRepository.findActiveByEmployeeId(employeeId).isPresent();
    }

    public void resetRegistration(String employeeId) {
        faceProfileRepository.deactivateAllForEmployee(employeeId);
        updateFaceRegisteredFlag(employeeId, false);
        log.info("Face registration RESET for employeeId={}", employeeId);
    }

    private void updateFaceRegisteredFlag(String employeeId, boolean registered) {
        var userMatch = userRepository.findByEmployeeId(employeeId);
        if (userMatch.isEmpty()) {
            try { userMatch = userRepository.findById(Long.valueOf(employeeId)); }
            catch (NumberFormatException ignored) { /* Employee IDs need not be numeric. */ }
        }
        userMatch.ifPresent(user -> {
            user.setFaceRegistered(registered);
            userRepository.save(user);
        });
    }

    public static double cosineSimilarity(double[] a, double[] b) {
        if (a.length != b.length) return 0.0;
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA < 1e-12 || normB < 1e-12) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public record FaceRegistrationResult(
            boolean success, Long profileId, Double qualityScore,
            Double livenessScore, String reason, String message) {
        public static FaceRegistrationResult success(Long id, double qs, double ls) {
            return new FaceRegistrationResult(true, id, qs, ls, null, "Face registered successfully");
        }
        public static FaceRegistrationResult failed(String reason, String message) {
            return new FaceRegistrationResult(false, null, null, null, reason, message);
        }
    }
}
