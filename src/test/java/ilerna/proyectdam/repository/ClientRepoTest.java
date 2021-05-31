package ilerna.proyectdam.repository;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.service.datamodel.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Conjunto de Tests DataJpa para la capa de Acceso a Datos ("Repository") de los Clientes
 * Se testean las operaciones CRUD en la BD a traves de la interfaz de acceso a datos
 * de la clase ClientRepo.
 *
 * Test 1 del  plan de pruebas- pág 16 del Trabajo
 *
 * @author Jose F. Bejarano
 * @see ClientServImpl
 * @see ClientRepo
 * @since 2021
 */

/** Configuracion para usar la bd en el disco en lugar de en memoria */
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)// Para que no se reemplace la configuracion del "application-test.properties"
//@Rollback(value = true) //Con false para que todos los cambios se reflejen en la BD si la usamos en disco
//@ActiveProfiles("test")

@DataJpaTest
public class ClientRepoTest {

    private static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);

    Client clientSaved;
    Optional<Client> cliente;
    @Autowired
    private ClientRepo repo;

    //Solo en ejecucion con Rollback = true, en otro caso rechazara el DNI al estar repetido
    @BeforeEach
    void saveOneClient(){
        clientSaved = repo.save(new Client("Sheldon", "Cooper", "F3753893D",
                "Avda Las Rosas", "sheldon@gamil.com", 928928928));
    }


    /**
     * Se comprueba que el cliente se haya creado correctamente en la BD
     */
    @Test
    @DisplayName("Deberia haberse creado un nuevo cliente")
    public void shouldClientSaved() {
        assertNotNull(clientSaved);
        cliente= repo.findById(clientSaved.getIdCliente());
        assertThat(clientSaved).usingRecursiveComparison().isEqualTo(cliente.get());
    }

    /**
     * Se comprueba que el cliente se modifique corrrectamente
     */
    @Test
    @DisplayName("Deberia modificarse el cliente cuyo id es dado")
    public void shouldClientChanged() {

        Client newClient = new Client("Peter", "Parker", "G2755589D",
                "Calle la que sea", "spiderman@gamil.com", 928928928);

        Optional<Client> clientModificado = repo.findById(clientSaved.getIdCliente())
                .map(Client -> {
                    Client.setNombre(newClient.getNombre());
                    Client.setNif(newClient.getNif());
                    Client.setApellidos(newClient.getApellidos());
                    Client.setDireccion(newClient.getDireccion());
                    Client.setEmail(newClient.getEmail());
                    Client.setTlfno(newClient.getTlfno());
                    return repo.save(Client);
                });
        assertThat(clientModificado.get()).usingRecursiveComparison().ignoringFields("idCliente").isEqualTo(newClient);
    }
    /**
     * Se testea que se borre un cliente correctamente dado un numero de id
     */
    @Test
    @DisplayName("El cliente deberia borrase")
    public void shouldDeleteClient() {
        repo.deleteById(clientSaved.getIdCliente());
        assertThat (false).isEqualTo(repo.findById(clientSaved.getIdCliente()).isPresent());
    }
}






