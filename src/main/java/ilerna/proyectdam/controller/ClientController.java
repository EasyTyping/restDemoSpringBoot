package ilerna.proyectdam.controller;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.datamodel.Client;
import ilerna.proyectdam.service.ClientServ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controlador del servicio Res para la clase Client
 * Atiende y procesa las peticiones Res de consulta, creacion, modificacion y borrado
 * que tengan que ver con los clientes de la empresa
 *
 * @author Jose F. Bejarano
 * @version 1.0
 * @since 2021
 */

@RestController
public class ClientController {

    private static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);
    @Autowired //interfaz de la capa de servicio que hace de fachada
    private ClientServ service;

    /**
     * Atiende las solicitudes de la lista completa de los clientes
     *
     * @return Lista de todos los clientes
     */
    @GetMapping("/clientes")
    public List<Client> getClientList() {
        LOG.info("Devolviendo la lista de clientes...");
        return service.findAll();
    }

    /**
     * Atiende las peticiones Get de consulta de un Cliente por su id
     *
     * @param id - Identificador de Cliente
     * @return Client  -   Cliente consultado
     * @throws MyNotFoundException
     */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id)
                .orElseThrow(() -> new MyNotFoundException("No se encuentra al cliente con id " + id)));
    }

    /**
     * Establece el endpoint que recibe y procesa las peticiones Post para la creacion de un nuevo Cliente
     *
     * @param newClient Objeto que proporciona la informacion del nuevo Cliente
     * @param result    Objeto que recibe el resultado de la validacion de Hibernate
     * @return ResponseEntity con valor TRUE si el Cliente ha sido creado en la BD
     * @throws UnprocessableEntityException En caso de que los datos no cumplan con el formato indicado en las anotaciones,
     *                                      no se devuelve la informacion completa del error generado por la validacion de Hibenate,
     *                                      en su lugar se lanza una Excepcion personalizada con Status 422 y con el mensaje de Error establecido,
     *                                      para que sea procesada por el Cliente del servicio
     */
    @PostMapping("/clientes")
    ResponseEntity<String> newClient(@Valid @RequestBody Client newClient, BindingResult result) throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.save(newClient);
            LOG.info("SERVER: Se ha creado un nuevo cliente");
            return ResponseEntity.status(HttpStatus.OK).body("El cliente se ha creado correctamente");
        } catch (DataIntegrityViolationException e) {
            LOG.error("Lanzando exception DataIntegrityViolation...");
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }

    /**
     * Se encarga de atender las peticiones delete y borrar el Cliente cuyo id
     * recibe como parametro
     *
     * @param id del Cliente a eliminar
     * @return  si falla se devuelve: "No class ilerna.proyectdam.service.datamodel.Client entity with id 1 exists!"
     */
    @DeleteMapping("/clientes/{id}")
    public void deleteClient(@PathVariable Integer id) { service.deleteById(id); }

        /**
         * Atiende las peticiones Put para la modificacion de un Cliente existente
         * @param id        del Cliente que se quiere modificar
         * @param newClient Objeto que proporciona la informacion del nuevo Cliente
         * @param result    Objeto que recibe el resultado de la validacion de Hibernate
         * @return TRUE si el Cliente ha sido creado en la BD
         * @throws UnprocessableEntityException En caso de que los datos no cumplan con el formato indicado en las anotaciones,
         *                                      no se devuelve la informacion completa del error generado por la validacion de Hibenate,
         *                                      en su lugar se lanza una Excepcion personalizada con Status 422 y con el mensaje de Error establecido,
         *                                      para que sea procesada por el Cliente del servicio
         * @throws MyNotFoundException          excepcion lanzada cuando no se encuentra el Cliente que se desea actualizar
         */
        @PutMapping("/clientes/{id}")
        public ResponseEntity<String> replaceClient (@Valid @RequestBody Client newClient, BindingResult result,
                @PathVariable Integer id) throws UnprocessableEntityException, MyNotFoundException {
            if (result.hasErrors())
                for (FieldError error : result.getFieldErrors())
                    throw new UnprocessableEntityException(error.getDefaultMessage());
            try {
                service.findById(id)
                        .map(Client -> {
                            Client.setNombre(newClient.getNombre());
                            Client.setApellidos(newClient.getApellidos());
                            Client.setDni(newClient.getDni());
                            Client.setDireccion(newClient.getDireccion());
                            Client.setEmail(newClient.getEmail());
                            Client.setTlfno(newClient.getTlfno());
                            return service.save(Client);
                        })
                        .orElseThrow(() -> new MyNotFoundException("No se encuentra al cliente con id " + id));
                return ResponseEntity.status(HttpStatus.OK).body("El cliente con id " + id + " se ha modificado correctamente");
            } catch (DataIntegrityViolationException e) {
                throw new UnprocessableEntityException(e.getRootCause().getMessage());
            }
        }
    }




