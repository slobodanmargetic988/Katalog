package com.katalog.katalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */
@EnableJpaRepositories("com.katalog.*")
@EntityScan("com.katalog.*")
@ComponentScan("com.katalog.*")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class KatalogApplication {//implements CommandLineRunner {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(KatalogApplication.class, args);
    }

}
