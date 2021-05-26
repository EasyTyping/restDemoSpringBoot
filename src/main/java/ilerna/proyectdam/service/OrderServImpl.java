package ilerna.proyectdam.service;

import ilerna.proyectdam.service.datamodel.Order;
import ilerna.proyectdam.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServImpl implements OrderServ {
    @Autowired
    private OrderRepo repo;

    @Override
    public List<Order> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Order save(Order o) {
        return repo.save(o);
    }

    @Override
    public void deleteById(Integer id) {repo.deleteById(id); }
}
