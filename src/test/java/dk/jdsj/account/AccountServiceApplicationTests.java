package dk.jdsj.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersInitializer.class)
@SpringBootTest
class AccountServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
