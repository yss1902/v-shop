package vshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import vshop.config.AppConfig;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class VShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(VShopApplication.class, args);
	}

}
