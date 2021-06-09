package ilerna.proyectdam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.datamodel.Client;
import ilerna.proyectdam.repository.ClientRepo;
import ilerna.proyectdam.service.ClientServ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@ActiveProfiles("test")
@DisplayName("Testeando endpoints en ClientController")
public class ClientControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);
    private static final String URL = "/clientes";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ClientRepo repo;
    @MockBean
    ClientServ service;

    @BeforeEach
    void saveOneClient() throws SQLException {
        repo.save(new Client("Pato", "Donald", "A4375589D",
                "Calle AstraZeneca", "disney@gamil.com", 928928928));
    }

    @Test
    @DisplayName("Creacion de un nuevo cliente")
    void testNewClient() throws Exception {

        Client client = new Client("Klark", "Kent", "B4378648D",
                "Adva de los krypton n 12  38006", "superman@gamil.com", 822822822);

        MvcResult result = mockMvc.perform(
                post(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(client))
        ).andExpect(status().isOk()).andReturn();

        assertThat("El cliente se ha creado correctamente").isEqualTo(result.getResponse().getContentAsString());

        LOG.info(result.getResponse().getContentAsString());
        LOG.info("Status http de la respuesta" + String.valueOf(result.getResponse().getStatus()));
    }

    @Test
    @DisplayName("Deberia modificarse el cliente")
    void testReplaceClient() throws Exception {

        int id=1;

        Client client = new Client("Peter", "Parker", "A4378648D",
                "Calle peticion put", "spiderman@gamil.com", 822822822);
        Client client2 = new Client("Pan", "Parker", "B3786489D",
                "La direccion ha sido cambiada", "spiderman@gamil.com", 822822822);

        Mockito.when(service.save(client)).thenReturn(client2);
        Mockito.when(service.findById(id)).thenReturn(Optional.of(client));

        MvcResult result= mockMvc.perform(
                put(URL+"/"+ id)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(client))
        ).andExpect(status().isOk()).andReturn();

        assertThat("El cliente con id 1 se ha modificado correctamente").isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Solicitando lista de clientes")
    void testGetClientList() throws Exception {

        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client("Howard", "Wolowitz", "66686489D",
                "Adva de los Patos n 15  38006", "howard@gamil.com", 622622612));
        clientList.add(new Client("Sheldon", "Cooper", "43786489D",
                "Adva de los Rosas 15  38006", "sheldon@gamil.com", 822822822));
        clientList.add(new Client("Leonard", "Hofstadter", "45766479D",
                "C/Filomena n 8 3 izq", "leonard@gamil.com", 922922922));
        clientList.add(new Client("Penny", "Hofstadter", "44768879D",
                "C/Nebraska n 10 4 derecha 39007", "penny@gamil.com", 522522522));

        Mockito.when(service.findAll()).thenReturn(clientList);

        //  RequestBuilder request = MockMvcRequestBuilders.get(URL);
        MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();
        String actualResponse = result.getResponse().getContentAsString();
        LOG.info(actualResponse);

        String expectedJsonResponse = mapper.writeValueAsString(clientList);
        LOG.info(expectedJsonResponse);

        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
        LOG.info("Status de la Respuesta: " + result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Borrar cliente por su id")
    void testDeleteClient() throws Exception {
        Integer idClient = 1;
        Mockito.doNothing().when(service).deleteById(idClient);
        mockMvc.perform(delete(URL + "/" + idClient)).andExpect(status().isOk());
        Mockito.verify(service, times(1)).deleteById(idClient);
    }

    //********************************** Wrong Requests ************************************

    /**
     * Comprobamos que se devuelve un mensaje con Status de ERROR 422
     * y que contiene una excepcion UnprocessableEntityException, cuando alguno de los datos
     * de la solicitud del cliente no es valido.
     * En el concreto caso realizamos una peticion de tipo POST para crear un nuevo cliente,
     * que contiene un nif no valido al no tener letra final. Podemos modificar el dato
     * no valido obteniendo la misma respuesta, por lo que con este test podemos probar todos los casos
     */
    @Test
    @DisplayName("Respuesta de error 422")
    void requestUnprocessableEntity() throws Exception {
        Client client = new Client("Klark", "Kent", "87864892",
                "Adva de los krypton n 12  38006", "superman@gamil.com", 822822822);

        MvcResult result = mockMvc.perform(
                post(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(client))
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(HttpStatus.UNPROCESSABLE_ENTITY.value()).isEqualTo(result.getResponse().getStatus());
        LOG.info("Status de la Respuesta: " + result.getResponse().getStatus());
        assertThat(UnprocessableEntityException.class).isEqualTo(result.getResolvedException().getClass());
        LOG.info("Excepcion de la Respuesta: " + result.getResolvedException().getClass());
        assertThat("Formato incorrecto de NIF").isEqualTo(result.getResolvedException().getMessage());
    }

    /**
     * Comprobamos que se devuelve un mensaje con Status de ERROR 404
     * y que contiene una excepcion MyNotFoundException, cuando el cliente no existe.
     */
    @Test
    @DisplayName("Respuesta de error 404")
    void requestMyNotFoundException() throws Exception {
        int idClient = 2;

        MvcResult result = mockMvc.perform(
                get(URL + "/" + idClient)
        ).andExpect(status().is4xxClientError()).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        LOG.info("Status de la Respuesta: " + result.getResponse().getStatus());
        assertThat(MyNotFoundException.class).isEqualTo(result.getResolvedException().getClass());
        LOG.info("Excepcion de la Respuesta: " + result.getResolvedException().getClass());
        assertThat("No se encuentra al cliente con id " + idClient).isEqualTo(result.getResolvedException().getMessage());
    }
}




