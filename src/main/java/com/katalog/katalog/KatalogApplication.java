package com.katalog.katalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;


import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class KatalogApplication implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
	public static void main(String[] args) {
		SpringApplication.run(KatalogApplication.class, args);
	}

        
    @Override
    public void run(String... args) throws Exception {
        String sql = "INSERT INTO webkatalog.test (id, ime) VALUES ("
                + "'1', 'pera')";
         
        int rows = jdbcTemplate.update(sql);
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
    }
        
        
        
}
