package org.sebastian.liceoyarah.ms.persons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "classpath:swagger.properties", encoding = "UTF-8")
public class MsPersonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPersonsApplication.class, args);
	}

}
