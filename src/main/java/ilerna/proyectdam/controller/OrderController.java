package ilerna.proyectdam.controller;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.datamodel.Order;
import ilerna.proyectdam.service.OrderServ;
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

/**
 * Controlador del servicio Res para la clase Order
 * Atiende y procesa las peticiones Res de consulta, creacion, modificacion y borrado
 * que tengan que ver con los pedidos
 * @author Jose F. Bejarano
 * @since 2021
 */

@RestController
public class OrderController {

    private final static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);
    @Autowired //interfaz de la capa de servicio que hace de fachada
    private OrderServ service;

    /**
     * Atiende las solicitudes de la lista completa de los pedidos
     * @return Lista de todos los pedidos
     */
    @GetMapping("/pedidos")
    List<Order> getOrderList() {
        LOG.info("Devolviendo la lista de pedidos...");
        return service.findAll();
    }

    /**
     * Establece el endpoint que recibe y procesa las peticiones Get de consulta de un pedido por su id
     * @param id - Identificador de pedido
     * @return ResponseEntity:  Order  -   pedido consultado
     */
    @GetMapping("/pedidos/{id}")
    ResponseEntity<Object> getOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id)
                .orElseThrow(() -> new MyNotFoundException("No se encuentra el pedido con id " + id)));
    }

    /**
     * Establece el endpoint que recibe y procesa las peticiones Post para la creacion de un nuevo pedido
     * @param newOrder Objeto que proporciona la informacion del nuevo pedido
     * @param result   Objeto que recibe el resultado de la validacion de Hibernate
     * @return ResponseEntity con status 200 si el pedido ha sido creado en la BD
     * @throws UnprocessableEntityException En caso de que los datos no cumplan con el formato indicado en las anotaciones,
     *                                      no se devuelve la informacion completa del error generado por la validacion de Hibenate,
     *                                      en su lugar se lanza una Excepcion personalizada con Status 422 y con el mensaje de Error establecido,
     *                                      para que sea procesada por el pedido del servicio
     */
    @PostMapping("/pedidos")
    ResponseEntity<Object> newOrder(@Valid @RequestBody Order newOrder, BindingResult result) throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.save(newOrder);
            LOG.info("SERVER: Se ha creado un nuevo pedido!!");
            return ResponseEntity.status(HttpStatus.OK).body("El pedido se ha creado correctamente");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }

    /**
     * Se encarga de atender las peticiones delete y borrar el pedido cuyo id
     * recibe como parametro
     *
     * @param id del pedido a eliminar
     */
    @DeleteMapping("/pedidos/{id}")
    void deleteOrder(@PathVariable Integer id) throws EmptyResultDataAccessException {
        service.deleteById(id);
    }

    /**
     * Atiende las peticiones Put para la modificacion de un pedido pedido existente
     *
     * @param id       del pedido que se quiere modificar
     * @param newOrder Objeto que proporciona la informacion del nuevo pedido
     * @param result   Objeto que recibe el resultado de la validacion de Hibernate
     * @return TRUE si el pedido ha sido creado en la BD
     * @throws UnprocessableEntityException En caso de que los datos no cumplan con el formato indicado en las anotaciones,
     *                                      no se devuelve la informacion completa del error generado por la validacion de Hibenate,
     *                                      en su lugar se lanza una Excepcion personalizada con Status 422 y con el mensaje de Error establecido,
     *                                      para que sea procesada por el pedido del servicio
     * @throws MyNotFoundException          excepcion lanzada cuando no se encuentra el pedido que se desea actualizar
     */
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
                        LOG.info("Se ha actualizado el pedido con id: " + id);
                        return service.save(Order);
                    }).orElseThrow(() -> new MyNotFoundException("No se encuentra al pedido con id " + id));
            return ResponseEntity.status(HttpStatus.OK).body("El cliente con id " + id + " se ha modificado correctamente");
        } catch (DataIntegrityViolationException e) {
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }
}
