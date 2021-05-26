package ilerna.proyectdam.repository;

import ilerna.proyectdam.service.datamodel.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepo extends JpaRepository<Item, Integer> {
    Optional<Item> findByNombreArticulo(String itemName);
}