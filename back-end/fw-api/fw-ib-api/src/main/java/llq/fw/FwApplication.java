package llq.fw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"llq.fw"})
public class FwApplication {

	public static void main(String[] args) {
		SpringApplication.run(FwApplication.class, args);
	}

}
