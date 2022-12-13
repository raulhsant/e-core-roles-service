package integration.testcontainers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MariaDBContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final MariaDBContainer<?> postgres = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.10.2"))
            .withDatabaseName("test_db")
            .withUsername("test-db-user")
            .withPassword("test-db-password");

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        postgres.start();

        TestPropertyValues.of(
                        "spring.datasource.url=" + postgres.getJdbcUrl(),
                        "spring.datasource.username=" + postgres.getUsername(),
                        "spring.datasource.password=" + postgres.getPassword())
                .applyTo(configurableApplicationContext.getEnvironment());
    }
}
