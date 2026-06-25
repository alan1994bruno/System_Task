package com.system.tasks;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:mysql://localhost:3306/meu_banco_de_dados?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", "usuario_admin", "senha_admin")
                .load();
        flyway.migrate();
        SpringApplication.run(Main.class, args);
    }
}
