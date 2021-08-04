package ilerna.proyectdam.service;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.repository.ItemRepo;
import ilerna.proyectdam.service.datamodel.Item;
import lombok.extern.java.Log;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Log
public class ItemServImpl implements ItemServ {

    public ItemServImpl(ItemRepo repo) {
        this.repo = repo;
    }
    private ItemRepo repo;

    @Override
    public List<Item> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Item> findById(Integer id) {
        log.info(">>>>>>>>>>>>> Consultando articulo");
        return repo.findById(id);
    }

    @Override
    public Item save(Item item) {
        log.info(">>>>>>>>>>>>> Creando articulo");
        return repo.save(item);
    }

    @Override
    public void deleteById(Integer id) {
        log.info(">>>>>>>>>>>>> Borrando articulo");
        repo.deleteById(id);
    }

    @Override
    public Optional<Item> findByNombreArticulo(String itemName) {
        return repo.findByNombreArticulo(itemName);
    }
}

