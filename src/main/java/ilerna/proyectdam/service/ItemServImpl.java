package ilerna.proyectdam.service;

import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.service.datamodel.Item;
import ilerna.proyectdam.repository.ItemRepo;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class ItemServImpl implements ItemServ {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);
    @Autowired
    private ItemRepo repo;

    @Override
    public List<Item> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Item> findById(Integer id) {
        LOG.info(">>>>>>>>>>>>> Consultando articulo");
        return repo.findById(id);
    }

    @Override
    public Item save(Item item) {
        LOG.info(">>>>>>>>>>>>> Creando articulo");
        return repo.save(item);
    }

    @Override
    public void deleteById(Integer id) {
        LOG.info(">>>>>>>>>>>>> Borrando articulo");
        repo.deleteById(id);
    }

    @Override
    public Optional<Item> findByNombreArticulo(String itemName) {
        return repo.findByNombreArticulo(itemName);
    }
}

