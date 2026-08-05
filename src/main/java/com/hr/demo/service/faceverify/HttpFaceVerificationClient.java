package com.hr.demo.service.faceverify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * HTTP implementation of {@link FaceVerificationClient} backed by Spring {@link RestClient}.
 */
@Service
public class HttpFaceVerificationClient implements FaceVerificationClient {

    private final RestClient restClient;
    private final boolean enabled;

    public HttpFaceVerificationClient(
            @Value("${app.face-verify.base-url:http://127.0.0.1:8000}") String baseUrl,
            @Value("${app.face-verify.enabled:true}") boolean enabled) {
        this.enabled = enabled;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        this.restClient = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public VerifyOutcome verify(String employeeId, MultipartFile faceImage) {
        try {
            VerifyResponse body = restClient.post()
                    .uri("/api/faces/verify")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(multipart(employeeId, null, faceImage))
                    .retrieve()
                    .body(VerifyResponse.class);
            if (body == null) {
                throw new FaceVerificationException("Face verify service returned an empty response");
            }
            return new VerifyOutcome(body.matched(), body.score(), body.threshold());
        } catch (RestClientResponseException ex) {
            throw friendly(ex);
        } catch (ResourceAccessException ex) {
            throw new FaceVerificationException("Face verification service unreachable: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void register(String employeeId, String name, MultipartFile faceImage) {
        try {
            restClient.post()
                    .uri("/api/faces/register")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(multipart(employeeId, name, faceImage))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException ex) {
            throw friendly(ex);
        } catch (ResourceAccessException ex) {
            throw new FaceVerificationException("Face verification service unreachable: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean isRegistered(String employeeId) {
        try {
            StatusResponse body = restClient.get()
                    .uri("/api/faces/{employeeId}", employeeId)
                    .retrieve()
                    .body(StatusResponse.class);
            return body != null && body.registered();
        } catch (RestClientResponseException ex) {
            throw friendly(ex);
        } catch (ResourceAccessException ex) {
            throw new FaceVerificationException("Face verification service unreachable: " + ex.getMessage(), ex);
        }
    }

    private MultiValueMap<String, Object> multipart(String employeeId, String name, MultipartFile image) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("employeeId", employeeId);
        if (name != null && !name.isBlank()) {
            body.add("name", name);
        }
        try {
            byte[] bytes = image.getBytes();
            ByteArrayResource resource = new ByteArrayResource(bytes) {
                @Override
                public String getFilename() {
                    String name = image.getOriginalFilename();
                    return name != null && !name.isBlank() ? name : "face.jpg";
                }
            };
            body.add("image", resource);
        } catch (IOException e) {
            throw new FaceVerificationException("Could not read uploaded face image", e);
        }
        return body;
    }

    private FaceVerificationException friendly(RestClientResponseException ex) {
        String body = ex.getResponseBodyAsString();
        String detail = null;
        if (body != null && body.contains("\"detail\"")) {
            int idx = body.indexOf("\"detail\"");
            String raw = body.substring(body.indexOf(':', idx) + 1).trim();
            detail = raw.replaceAll("^\"|\"$", "");
        }
        if (detail == null || detail.isBlank()) {
            detail = ex.getMessage();
        }
        return new FaceVerificationException(detail, ex);
    }

    record VerifyResponse(boolean matched, double score, double threshold) {}

    record StatusResponse(boolean registered) {}
}
