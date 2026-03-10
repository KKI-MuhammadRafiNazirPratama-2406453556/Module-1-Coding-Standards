package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            EshopApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(EshopApplication.class, new String[]{}));
        }
    }
}
