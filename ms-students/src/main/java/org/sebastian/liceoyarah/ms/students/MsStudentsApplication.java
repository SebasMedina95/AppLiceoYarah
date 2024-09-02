package org.sebastian.liceoyarah.ms.students;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@EnableFeignClients
@SpringBootApplication
@PropertySource(value = "classpath:swagger.properties", encoding = "UTF-8")
public class MsStudentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsStudentsApplication.class, args);
	}

}
