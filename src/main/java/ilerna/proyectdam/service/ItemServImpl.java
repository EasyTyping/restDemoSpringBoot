package ilerna.proyectdam.service;

import ilerna.proyectdam.service.datamodel.Item;
import ilerna.proyectdam.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServImpl implements ItemServ {

    @Autowired
    private ItemRepo repo;

    @Override
    public List<Item> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Item save(Item item) {
        return repo.save(item);
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Item> findByNombreArticulo(String itemName) {
        return repo.findByNombreArticulo(itemName);
    }
}

