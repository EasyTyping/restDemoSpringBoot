package ilerna.proyectdam.service;

import ilerna.proyectdam.service.datamodel.Item;

import java.util.List;
import java.util.Optional;

public interface ItemServ {

    List<Item> findAll();
    Optional<Item> findByNombreArticulo(String itemName);
    Optional<Item> findById(Integer id);
    Item save(Item item);
    void deleteById(Integer id);
}
