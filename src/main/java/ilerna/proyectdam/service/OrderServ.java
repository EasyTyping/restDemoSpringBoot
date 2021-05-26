package ilerna.proyectdam.service;

import ilerna.proyectdam.service.datamodel.Order;

import java.util.List;
import java.util.Optional;

public interface OrderServ {
    List<Order> findAll();
    Optional<Order> findById(Integer id);
    Order save(Order o);
    void deleteById(Integer id);
}
