package pe.edu.vallegrande.barberia_macha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "pe.edu.vallegrande.barberia_macha.model") // Ajusta el paquete según tu estructura
public class    BarberiaMachaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarberiaMachaApplication.class, args);
    }

}
