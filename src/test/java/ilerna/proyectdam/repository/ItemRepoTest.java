package ilerna.proyectdam.repository;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.service.datamodel.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Conjunto de Tests DataJpa para la capa de Acceso a Datos ("Repository") de los Articulos
 * Se testean las operaciones CRUD en la BD a traves de la interfaz de acceso a datos
 * de la clase ItemRepo.
 *
 * Test 1 del  plan de pruebas- pág 16 del Trabajo
 *
 * @author Jose F. Bejarano
 * @see ItemServImpl
 * @see ItemRepo
 * @since 2021
 */

/** Configuracion para usar la bd en el disco en lugar de en memoria */
// Para que no se reemplace la configuracion del "application-test.properties"
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(value = false) //Asi evitamos el RollBack para que los cambios se reflejen en la BD si la usamos en disco
//@ActiveProfiles("test")

@DataJpaTest
public class ItemRepoTest {

    private static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);

    Item itemSaved;
    Optional<Item> item;
    @Autowired
    private ItemRepo repo;

    @BeforeEach
    public void savedItem() {
        itemSaved = repo.save(new Item("Jordan AIR XXXV", " Zapatillas de baloncesto", 179.95f, 25));
    }

    /**
     * Se comprueba que el articulo se haya creado correctamente en la BD
     */
    @Test
    @DisplayName("Deberia haberse creado un articulo")
    public void shouldItemSaved() {
        assertNotNull(itemSaved);
        item = repo.findById(itemSaved.getIdArticulo());
        assertThat(itemSaved).usingRecursiveComparison().isEqualTo(item.get());
    }

    /**
     * Se comprueba que el articulo se modifique corrrectamente
     */
    @Test
    @DisplayName("Deberia modificarse el articulo cuyo id es dado")
    public void shouldItemChanged() {

        Item newItem = new Item("Acsis GEL EXCITE 8 ", "Zapatillas de running neutras",
                180.35f, 5);

        Optional<Item> itemModificado = repo.findById(itemSaved.getIdArticulo())
                .map(Item -> {
                    Item.setNombreArticulo(newItem.getNombreArticulo());
                    Item.setDescripcion(newItem.getDescripcion());
                    Item.setStock(newItem.getStock());
                    Item.setPrecioUnidad(newItem.getPrecioUnidad());
                    return repo.save(Item);
                });
        assertThat(itemModificado.get()).usingRecursiveComparison().ignoringFields("idArticulo").isEqualTo(newItem);
    }

     /**
     * Se testea que se borre un articulo correctamente dado un numero de id
     */
    @Test
    @DisplayName("El articulo deberia borrase")
    public void shouldDeleteItem() {
        repo.deleteById(itemSaved.getIdArticulo());
        assertThat (false).isEqualTo(repo.findById(itemSaved.getIdArticulo()).isPresent());
    }
}






