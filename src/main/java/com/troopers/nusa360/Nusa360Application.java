package com.troopers.nusa360;

import com.troopers.nusa360.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Nusa360Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Nusa360Application.class, args);
    }

}
