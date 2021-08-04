package ilerna.proyectdam.service;

import ilerna.proyectdam.repository.ItemRepo;
import ilerna.proyectdam.repository.OrderRepo;
import ilerna.proyectdam.service.datamodel.Item;
import ilerna.proyectdam.service.datamodel.Order;
import ilerna.proyectdam.service.datamodel.OrderLine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderServImpl implements OrderServ {

    private final OrderRepo repo;
    private final ItemRepo repoItem;
    List<OrderLine> orderLineList;

    public OrderServImpl(OrderRepo repo, ItemRepo repoItem) {
        this.repo = repo;
        this.repoItem = repoItem;
    }

    @Override
    public List<Order> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Order> findById(Integer id) {
        return repo.findById(id);
    }


    //these two methods are temporary...
    @Override
    public Order save(Order o) {
         orderLineList= o.getLineasPedido();
        for(OrderLine line : orderLineList)
            repoItem.save(line.getArticulo());
        return repo.save(o);
    }

    @Override
    public void deleteById(Integer id) throws NoSuchElementException  {
        Optional<Order> optionalOrder= findById(id);
        Order order= optionalOrder.get();
        orderLineList= order.getLineasPedido();
        for(OrderLine line : orderLineList){
            Item item= repoItem.findById(line.getArticulo().getIdArticulo()).get();
//            System.out.println("Linea Cantidad: "+ line.getCantidad());
//            System.out.println("Articulo: "+ item.getNombreArticulo() + "| Stock: " + item.getStock());
            item.setStock(item.getStock() + line.getCantidad());
            repoItem.save(item);
        }
        repo.deleteById(id);
    }

}
