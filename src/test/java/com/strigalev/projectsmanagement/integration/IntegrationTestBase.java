package com.strigalev.projectsmanagement.integration;


import com.strigalev.projectsmanagement.integration.initializer.Postgres;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {
        Postgres.Initializer.class
})
@Transactional
public abstract class IntegrationTestBase {

    @BeforeAll
    static void init() {
        Postgres.container.start();
    }
}
