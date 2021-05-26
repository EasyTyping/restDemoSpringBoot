package ilerna.proyectdam.service;

import ilerna.proyectdam.service.datamodel.Client;

import java.util.List;
import java.util.Optional;

public interface ClientServ {

    List<Client> findAll();
    Optional<Client> findById(Integer id);
    Client save(Client c);
    void deleteById(Integer id);
}
