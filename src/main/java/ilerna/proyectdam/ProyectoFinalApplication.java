package ilerna.proyectdam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoFinalApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProyectoFinalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Inicializando el servicio Rest del Proyecto de Aplicaciones Multiplataforma... Hola Mundo!!");
    }
}
