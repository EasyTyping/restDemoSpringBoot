package ilerna.proyectdam.controller;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.ItemServ;
import ilerna.proyectdam.service.datamodel.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador que gestiona las peticiones http para los articulos (Clase Item) de la aplicacion
 *
 * @author Jose F. Bejarano
 * @version 1.0
 * @since 2021
 */

@RestController
public class ItemController {

    private final static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);
    @Autowired
    private ItemServ service;

 /*   public ItemController(ItemServ service) {
        this.service = service;
    }*/

    /**
     * Busca y devuelve un articulo por su nombre
     *
     * @param name String con el nombre del articulo
     * @return Objeto de la clase Item que contiene el articulo cuyo nombre se busca
     */
    @GetMapping("/articulo/{name}")
    Optional<Item> findByItemName(@PathVariable String name) {
        return service.findByNombreArticulo(name);
    }

    /**
     * Atiende las solicitudes de la lista completa de los articulos
     *
     * @return Lista de todos los articulos
     */
    @GetMapping("/articulos")
    List<Item> getItemList() {
       // LOG.info("Devolviendo la lista de articulos...");
        return service.findAll();
    }

    /**
     * Establece el endpoint que recibe y procesa las peticiones Get de consulta de un articulo por su id
     *
     * @param id - Identificador de articulo
     * @return Item  -   articulo consultado
     */
    @GetMapping("/articulos/{id}")
    ResponseEntity<Object> getItem(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id)
                .orElseThrow(() -> new MyNotFoundException("No se encuentra el articulo con id " + id)));
    }

    /**
     * Establece el endpoint que recibe y procesa las peticiones Post para la creacion de un nuevo articulo
     *
     * @param newItem Objeto de la clase Item que proporciona la informacion del nuevo articulo
     * @param result  Objeto que recibe el resultado de la validacion de Hibernate
     * @return ResponseEntity, con mensaje de confirmacion y status 400 si el articulo ha sido creado en la BD
     * @throws UnprocessableEntityException En caso de que los datos no cumplan con el formato indicado en las anotaciones,
     *                                      no se devuelve la informacion completa del error generado por la validacion de Hibenate,
     *                                      en su lugar se lanza una Excepcion personalizada con Status 422 y con el mensaje de Error establecido,
     *                                      para que sea procesada por el cliente del servicio
     */
    @PostMapping("/articulos")
    ResponseEntity<Object> newItem(@Valid @RequestBody Item newItem, BindingResult result) throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.save(newItem);
            LOG.info(">>>>>>>>>>>>Se ha creado un nuevo articulo");
            return ResponseEntity.status(HttpStatus.OK).body("El articulo se ha creado correctamente");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }

    /**
     * Se encarga de atender las peticiones 'delete' y borrar el articulo cuyo id
     * recibe como parametro
     *
     * @param id del articulo a eliminar
     */
    @DeleteMapping("/articulos/{id}")
    void deleteItem(@PathVariable Integer id) throws MyNotFoundException {
            try{
                service.deleteById(id);
            }catch(EmptyResultDataAccessException e){
                throw new MyNotFoundException("No hay nada que borrar, el artículo con "+ id + " no existe");
            }
    }

    /**
     * Atiende las peticiones Put para la modificacion de un articulo articulo existente
     *
     * @param id      del articulo que se quiere modificar
     * @param newItem Objeto de la clase Item que proporciona la informacion del nuevo articulo
     * @param result  Objeto que recibe el resultado de la validacion de Hibernate
     * @return TRUE si el articulo ha sido creado en la BD
     * @throws UnprocessableEntityException En caso de que los datos no cumplan con el formato indicado en las anotaciones,
     *                                      no se devuelve la informacion completa del error generado por la validacion de Hibenate,
     *                                      en su lugar se lanza una Excepcion personalizada con Status 422 y con el mensaje de Error establecido,
     *                                      para que sea procesada por el articulo del servicio
     * @throws MyNotFoundException          excepcion lanzada cuando no se encuentra el articulo que se desea actualizar
     */
    @PutMapping("/articulos/{id}")
    ResponseEntity<Object> replaceItem(@Valid @RequestBody Item newItem, BindingResult result,
                                       @PathVariable Integer id) throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.findById(id)
                    .map(Item -> {
                        Item.setNombreArticulo(newItem.getNombreArticulo());
                        Item.setDescripcion(newItem.getDescripcion());
                        Item.setStock(newItem.getStock());
                        Item.setPrecioUnidad(newItem.getPrecioUnidad());
                        LOG.info("Se ha actualizado el articulo con id: " + id);
                        return service.save(Item);
                    })
                    .orElseThrow(() -> new MyNotFoundException("No se encuentra el artículo con id " + id));
            return ResponseEntity.status(HttpStatus.OK).body("El cliente se ha actualizado");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }

}