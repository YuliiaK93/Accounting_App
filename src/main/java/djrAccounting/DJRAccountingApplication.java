package djrAccounting;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DJRAccountingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DJRAccountingApplication.class, args);
    }

    @Bean
    public ModelMapper mapper() {

        return new ModelMapper();
    }
}
