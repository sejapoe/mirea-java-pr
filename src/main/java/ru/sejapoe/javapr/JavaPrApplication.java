package ru.sejapoe.javapr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.sejapoe.javapr.security.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class JavaPrApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaPrApplication.class, args);
    }

}
