package ilerna.proyectdam.controller;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.datamodel.Client;
import ilerna.proyectdam.service.ClientServ;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
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

@RestController
@NoArgsConstructor
@Log
public class ClientController {

    private ClientServ service;

    @Autowired
    public ClientController(ClientServ service) {
        this.service = service;
    }

    @GetMapping("/clientes")
    public List<Client> getClientList() {
        return service.findAll();
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Object> getClient(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id)
                .orElseThrow(() -> new MyNotFoundException("No se encuentra al cliente con id " + id)));
    }

    @PostMapping("/clientes")
    ResponseEntity<String> newClient(@Valid @RequestBody Client newClient, BindingResult result)
            throws UnprocessableEntityException {
        if (result.hasErrors())
            for (FieldError error : result.getFieldErrors())
                throw new UnprocessableEntityException(error.getDefaultMessage());
        try {
            service.save(newClient);
            log.info("SERVER: Se ha creado un nuevo cliente");
            return ResponseEntity.status(HttpStatus.OK).body("El cliente se ha creado correctamente");
        } catch (DataIntegrityViolationException e) {
            log.warning("Lanzando exception DataIntegrityViolation...");
            throw new UnprocessableEntityException(e.getRootCause().getMessage());
        }
    }

    @DeleteMapping("/clientes/{id}")
    public void deleteClient(@PathVariable Integer id) { service.deleteById(id); }


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
                            Client.setNif(newClient.getNif());
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




