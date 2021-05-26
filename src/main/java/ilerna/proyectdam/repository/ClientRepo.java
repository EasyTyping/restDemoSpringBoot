package ilerna.proyectdam.repository;

import ilerna.proyectdam.service.datamodel.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client, Integer> {
}
