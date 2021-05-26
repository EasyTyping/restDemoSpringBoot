package ilerna.proyectdam.repository;

import ilerna.proyectdam.service.datamodel.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
}
