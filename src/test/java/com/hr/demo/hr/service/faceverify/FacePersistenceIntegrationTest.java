package com.hr.demo.hr.service.faceverify;

import com.hr.demo.hr.entity.FaceProfileEntity;
import com.hr.demo.hr.repository.FaceProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

/** Explicit opt-in; every fixture write is rolled back against the configured DB. */
@SpringBootTest
@Transactional
@EnabledIfEnvironmentVariable(named="FACE_TEST_DB", matches="true")
class FacePersistenceIntegrationTest {
    @Autowired FaceProfileRepository faces;
    @Autowired EntityManager em;

    @Test void embeddingRoundTripsThroughPostgres() {
        String employee = "__test_face_" + UUID.randomUUID();
        double[] embedding = new double[512];
        embedding[0] = 1.0;
        faces.saveAndFlush(FaceProfileEntity.builder().employeeId(employee).embedding(embedding)
                .qualityScore(.9).livenessScore(.9).active(true).build());
        em.clear();
        assertThat(faces.findActiveByEmployeeId(employee).orElseThrow().getEmbedding()).containsExactly(embedding);
    }
}
