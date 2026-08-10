package com.hr.demo.service.faceverify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FaceProcessResponse(
        boolean success,
        String code,
        List<Double> embedding,
        int faceCount,
        double qualityScore,
        double livenessScore,
        boolean livenessPassed,
        double maskRisk,
        String modelName,
        String modelVersion,
        int embeddingDimension,
        String message,
        String reason
) {}
