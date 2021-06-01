package ilerna.proyectdam.service;

import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.repository.ItemRepo;
import ilerna.proyectdam.service.datamodel.Item;
import ilerna.proyectdam.service.datamodel.Order;
import ilerna.proyectdam.repository.OrderRepo;
import ilerna.proyectdam.service.datamodel.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServImpl implements OrderServ {
    @Autowired
    private OrderRepo repo;
    @Autowired
    private ItemRepo repoItem;

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
        List<OrderLine> orderLineList= o.getLineasPedido();
        for(OrderLine line : orderLineList){
               Item item= repoItem.findById(line.getArticulo().getIdArticulo()).get();
               item.setStock(line.getArticulo().getStock());
               repoItem.save(item);
        }
        return repo.save(o);
    }

    @Override
    public void deleteById(Integer id) {repo.deleteById(id); }
}
