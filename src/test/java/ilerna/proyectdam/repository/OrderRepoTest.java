package ilerna.proyectdam.repository;

import ilerna.proyectdam.service.datamodel.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Conjunto de Tests DataJpa para la capa de Acceso a Datos ("Repository") de los Pedidos
 * Se testean las operaciones CRUD en la BD a traves de la interfaz de acceso a datos
 * de la clase OrderRepo.
 *
 * Test 1 del  plan de pruebas- pág 16 del Trabajo
 *
 * @author Jose F. Bejarano
 * @see OrderServImpl
 * @see OrderRepo
 * @since 2021
 */

/** Configuracion para usar la bd en el disco en lugar de en memoria */
// Para que no se reemplace la configuracion del "application-test.properties"
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false) //Asi evitamos el RollBack para que los cambios se reflejen en la BD si la usamos en disco
@ActiveProfiles("test")
@DataJpaTest
public class OrderRepoTest {

    Order orderSaved;
    Optional<Order> order;
    @Autowired
    private OrderRepo repo;

    @BeforeEach
    public void savedOrder() {
        orderSaved = repo.save(new Order(LocalDate.now(), 23.4f,  43.2f, 179.95f));
    }

    /**
     * Se comprueba que el pedido se haya creado correctamente en la BD
     */
    @Test
    @DisplayName("Deberia haberse creado un pedido")
    public void shouldorderSaved() {
        assertNotNull(orderSaved);
        order = repo.findById(orderSaved.getIdPedido());
        assertThat(orderSaved).usingRecursiveComparison().isEqualTo(order.get());
    }

    /**
     * Se comprueba que el articulo se modifique corrrectamente
     */
    @Test
    @DisplayName("Deberia modificarse el pedido cuyo id es dado")
    public void shouldOrderChanged() {

        Order neworder = new Order(LocalDate.now(), 23.4f, 67.2f, 304f);

        Optional<Order> orderModificado = repo.findById(orderSaved.getIdPedido())
                .map(order -> {
                    order.setFecha(neworder.getFecha());
                    order.setIvaPedido(neworder.getIvaPedido());
                    order.setPorcentajeIva(neworder.getPorcentajeIva());
                    order.setTotal(neworder.getTotal());
                    return repo.save(order);
                });
        assertThat(orderModificado.get()).usingRecursiveComparison().ignoringFields("idPedido").isEqualTo(neworder);
    }


    /**
     * Se testea que se borre un pedido correctamente dado un numero de id
     */
    @Test
    @DisplayName("Deberia borrase el pedido cuyo id es dado")
    public void shouldDeleteorder() {
        repo.deleteById(orderSaved.getIdPedido());
        assertThat (false).isEqualTo(repo.findById(orderSaved.getIdPedido()).isPresent());
    }
}
