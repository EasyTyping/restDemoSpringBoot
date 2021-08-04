package ilerna.proyectdam.controller;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.ItemServ;
import ilerna.proyectdam.service.ItemServImpl;
import ilerna.proyectdam.service.datamodel.Item;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
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
 * @author Jose F. Bejarano
 * @version 1.0
 * @since 2021
 */

@RestController
@Log
@NoArgsConstructor
public class ItemController {

    private ItemServ service;

    @Autowired
    public ItemController(ItemServ service) {
        this.service = service;
    }

    @GetMapping("/articulo/{name}")
    Optional<Item> findByItemName(@PathVariable String name) {
        return service.findByNombreArticulo(name);
    }

    @GetMapping("/articulos")
    List<Item> getItemList() {
       // log.info("Devolviendo la lista de articulos...");
        return service.findAll();
    }

    @GetMapping("/articulos/{id}")
    ResponseEntity<Object> getItem(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id)
                .orElseThrow(() -> new MyNotFoundException("No se encuentra el articulo con id " + id)));
    }

    @PostMapping("/articulos")
    ResponseEntity<Object> newItem(@Valid @RequestBody Item newItem, BindingResult result) throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.save(newItem);
            log.info(">>>>>>>>>>>>Se ha creado un nuevo articulo");
            return ResponseEntity.status(HttpStatus.OK).body("El articulo se ha creado correctamente");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }


    @DeleteMapping("/articulos/{id}")
    void deleteItem(@PathVariable Integer id) throws MyNotFoundException {
            try{
                service.deleteById(id);
            }catch(EmptyResultDataAccessException e){
                throw new MyNotFoundException("No hay nada que borrar, el artículo con "+ id + " no existe");
            }
    }

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
                        log.info("Se ha actualizado el articulo con id: " + id);
                        return service.save(Item);
                    })
                    .orElseThrow(() -> new MyNotFoundException("No se encuentra el artículo con id " + id));
            return ResponseEntity.status(HttpStatus.OK).body("El cliente se ha actualizado");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }

}