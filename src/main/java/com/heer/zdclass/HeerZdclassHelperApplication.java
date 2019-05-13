package com.heer.zdclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author heer_
 */
@SpringBootApplication
@EnableAutoConfiguration
//@MapperScan(value = "com.heer.zdclass.mapper")
public class HeerZdclassHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeerZdclassHelperApplication.class, args);
    }

}
