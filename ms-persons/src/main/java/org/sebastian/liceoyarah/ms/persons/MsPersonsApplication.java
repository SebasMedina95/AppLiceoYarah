package org.sebastian.liceoyarah.ms.persons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@EnableDiscoveryClient
@SpringBootApplication
@PropertySource(value = "classpath:swagger.properties", encoding = "UTF-8")
public class MsPersonsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPersonsApplication.class, args);
	}

}
