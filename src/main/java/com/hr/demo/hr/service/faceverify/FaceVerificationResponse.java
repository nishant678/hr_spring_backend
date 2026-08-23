package com.hr.demo.hr.service.faceverify;

public record FaceVerificationResponse(
        boolean matched, double score, double threshold,
        double qualityScore, double livenessScore,
        String reason, String message) {

    static FaceVerificationResponse of(boolean matched, double score, double threshold,
                                        double quality, double liveness) {
        return new FaceVerificationResponse(matched, score, threshold, quality, liveness,
                matched ? null : "FACE_NOT_MATCHED",
                matched ? "Face verified" : "Face does not match");
    }

    static FaceVerificationResponse failed(String reason, String message) {
        return new FaceVerificationResponse(false, 0, 0, 0, 0, reason, message);
    }
}
