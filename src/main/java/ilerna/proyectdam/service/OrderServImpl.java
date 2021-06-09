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
import org.springframework.dao.EmptyResultDataAccessException;
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
    List<OrderLine> orderLineList;

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
         orderLineList= o.getLineasPedido();
        for(OrderLine line : orderLineList)
            repoItem.save(line.getArticulo());
        return repo.save(o);
    }

    @Override
    public void deleteById(Integer id)  {
        Optional<Order> optionalOrder= findById(id);
        Order order= optionalOrder.get();
        orderLineList= order.getLineasPedido();
        for(OrderLine line : orderLineList){
            Item item= repoItem.findById(line.getArticulo().getIdArticulo()).get();//Optional<>
//            System.out.println("Linea Cantidad: "+ line.getCantidad());
//            System.out.println("Articulo: "+ item.getNombreArticulo() + "| Stock: " + item.getStock());
            item.setStock(item.getStock() + line.getCantidad());
            repoItem.save(item);
        }
        repo.deleteById(id);
    }

}
