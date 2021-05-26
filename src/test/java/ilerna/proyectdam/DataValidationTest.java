package ilerna.proyectdam;

import ilerna.proyectdam.service.datamodel.Client;
import ilerna.proyectdam.service.datamodel.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**Tests que comprueban que el sistema realice la validacion  de datos
 * correctamente. Se comprueban las validaciones de las clases del modelo de datos.
 *
 * @author Jose F. Bejarano
 * @since 2021
 *  */
public class DataValidationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    //****************** Testeando la validacion de datos de las entidades CLIENT ********************

    /**Se testean los campos obligatorios de las Entidades Client
     * nombre : El nombre es obligatorio
     * email : El email es obligatorio
     * dni : El DNI es obligatorio */
    @Test
    void checkClientNotBlankFields()  {

        Set<ConstraintViolation<Client>> violations = validator.validate(new Client());
        Assertions.assertEquals(3, violations.size());
        violations.forEach(v -> LOG.warn(
                v.getPropertyPath() + " : " + v.getMessageTemplate() ));
    }

    /**
     * Test para comprobar que se realizan las validaciones de los datos de
     * las entidades Client, comprobamos que el numero de violaciones  coincide con las introducidas
     * y las imprimimos por pantalla (podemos introducir cualesquiera otras o cambiar las existentes):
     *
     * dni : Formato incorrecto de DNI. Le falta la letra.
     * nombre : El tamaño del campo nombre no cumple los requisitos, min=3
     * email : Usamos un email sin @
     * apellidos : El apellido supera los 50 caracteres
     */
    @Test
    public void checkDataClientFields() {

        Set<ConstraintViolation<Client>> violations = validator.validate(new Client("12",
                "ERROR01234567890123456789012345678901234567890123456789",
                "43865489", "Adva de los krypton n 12  38006", "supermangamil.com", 822822822));
        Assertions.assertEquals(4, violations.size());
        violations.forEach(v -> LOG.warn(
                v.getPropertyPath() + " : " + v.getMessage()));

    }

    //****************** Testeando validacion de datos de las entidades ITEM ********************

    /** Se testean los campos obligatorios de las Entidades Item
            precioUnidad : El articulo debe tener un precio por unidad
            nombreArticulo : El nombre del articulo es obligatorio*
            stock : El stock no puede estar vacio, tiene que contener  0 unidades*/
    @Test
    void checkItemNotBlankFields()  {
        Set<ConstraintViolation<Item>> violations = validator.validate(new Item());
        Assertions.assertEquals(3, violations.size());
        violations.forEach(v -> LOG.warn(
                v.getPropertyPath() + " : " + v.getMessageTemplate() ));
    }
}


