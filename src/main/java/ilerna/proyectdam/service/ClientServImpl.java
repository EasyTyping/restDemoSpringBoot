package ilerna.proyectdam.service;

import ilerna.proyectdam.service.datamodel.Client;
import ilerna.proyectdam.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServImpl implements ClientServ {

    @Autowired
    private ClientRepo repo;

    @Override
    public List<Client> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Client save(Client cliente) {
        return repo.save(cliente);
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
}
