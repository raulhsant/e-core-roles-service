package integration;

import com.ecore.rolesservice.RolesServiceApplication;
import integration.testcontainers.MariaDBContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = RolesServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
@ContextConfiguration(initializers = MariaDBContextInitializer.class)
public abstract class AbstractIT {
}
