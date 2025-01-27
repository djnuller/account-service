package dk.jdsj.account;

import org.springframework.boot.SpringApplication;

public class TestAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(AccountServiceApplication::main).with(TestcontainersInitializer.class).run(args);
    }

}
