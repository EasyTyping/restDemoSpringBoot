package ilerna.proyectdam.controller;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.datamodel.Order;
import ilerna.proyectdam.service.OrderServ;
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

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Jose F. Bejarano
 * @since 2021
 */
@RestController
@Log
public class OrderController {

    private OrderServ service;

    @Autowired
    public OrderController(OrderServ service) {
        this.service = service;
    }

    @GetMapping("/pedidos")
    List<Order> getOrderList() {
    //    log.info(">>>>>>>> Devolviendo la lista de pedidos...");
        return service.findAll();
    }

    @GetMapping("/pedidos/{id}")
    ResponseEntity<Object> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id)
                .orElseThrow(() -> new MyNotFoundException("No se encuentra el pedido con id " + id)));
    }


    @PostMapping("/pedidos")
    ResponseEntity<Object> newOrder(@Valid @RequestBody Order newOrder, BindingResult result) throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.save(newOrder);
            log.info("SERVER: Se ha creado un nuevo pedido!!");
            return ResponseEntity.status(HttpStatus.OK).body("El pedido se ha creado correctamente");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }


    @DeleteMapping("/pedidos/{id}")
    void  deleteOrder(@PathVariable Integer id) throws MyNotFoundException {
        try {
            service.deleteById(id);
        }catch (EmptyResultDataAccessException | NoSuchElementException e){
            throw new MyNotFoundException("No hay nada que borrar, la entidad con id "+ id + " no existe");
        }
    }


    @PutMapping("/pedidos/{id}")
    ResponseEntity<Object> replaceOrder(@Valid @RequestBody Order newOrder, BindingResult result,
                                        @PathVariable Integer id) throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.findById(id)
                    .map(Order -> {
                        Order.setFecha(newOrder.getFecha());
                        Order.setIvaPedido(newOrder.getIvaPedido());
                        Order.setTotal(newOrder.getTotal());
                        Order.setPorcentajeIva(newOrder.getPorcentajeIva());
                        log.info("Se ha actualizado el pedido con id: " + id);
                        return service.save(Order);
                    }).orElseThrow(() -> new MyNotFoundException("No se encuentra al pedido con id " + id));
            return ResponseEntity.status(HttpStatus.OK).body("El cliente con id " + id + " se ha modificado correctamente");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }
}
