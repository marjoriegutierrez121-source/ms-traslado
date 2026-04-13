package pe.inpe.ms_traslado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTrasladoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTrasladoApplication.class, args);
	}

}
