package ilerna.proyectdam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.datamodel.Item;
import ilerna.proyectdam.service.ItemServ;
import ilerna.proyectdam.service.datamodel.Order;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@ActiveProfiles("test")
@DisplayName("Testeando endpoints en ItemController")
class ItemControllerTest {

    private final static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);

    private static final String URL = "/articulos";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemServ service;

    @Test
    @DisplayName("Creación de un nuevo articulo")
    void newItem() throws Exception {

        Item item=  new Item("Silla Confort", "La mejor silla si estas mucho tiempo sentado", 60.5f, 60);

        MvcResult result = mockMvc.perform(
                post(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(item))
        ).andExpect(status().isOk()).andReturn();

        assertThat("El articulo se ha creado correctamente").isEqualTo(result.getResponse().getContentAsString());

        LOG.info(result.getResponse().getContentAsString());
        //    LOG.info(String.valueOf(result.getResolvedException()));
        LOG.info(String.valueOf(result.getResponse().getStatus()));
    }

    @Test
    @DisplayName("Consultar lista articulos")
    void getItemsList() throws Exception {

        List<Item> itemsList = new ArrayList<>();
        itemsList.add(new Item("New Balance", "Ideales para la jungla urbana", 60.5f, 60));
        itemsList.add(new Item("Nike Jordan", "Las campeonas del basket", 150.23f, 30));
        itemsList.add(new Item("Pepe Jeans", "Zapatillas de vestir", 75.5f, 20));

        Mockito.when(service.findAll()).thenReturn(itemsList);

        //  RequestBuilder request = MockMvcRequestBuilders.get(URL);
        MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();
        String actualResponse = result.getResponse().getContentAsString();
        LOG.info(actualResponse);

        String expectedJsonResponse = mapper.writeValueAsString(itemsList);
        LOG.info(expectedJsonResponse);

        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
        LOG.info("Status de la Respuesta: " + result.getResponse().getStatus());
    }

    @Test
    @DisplayName("Consultar art por id")
    void getItemById() throws Exception {

       Item item= new Item("Silla Confort", "La mejor silla si estas mucho tiempo sentado", 60.5f, 60);
        int id=1;

        Mockito.when(service.findById(id)).thenReturn(java.util.Optional.of(item));

        //  RequestBuilder request = MockMvcRequestBuilders.get(URL);
        MvcResult result = mockMvc.perform(get(URL+"/"+ id)).andExpect(status().isOk()).andReturn();
        String actualResponse = result.getResponse().getContentAsString();
        LOG.info(actualResponse);

        String expectedJsonResponse = mapper.writeValueAsString(java.util.Optional.of(item));
        LOG.info(expectedJsonResponse);

        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
        LOG.info("Status de la Respuesta: " + result.getResponse().getStatus());
        LOG.info("Se ha obtenido el articulo con id: "+ id);
    }

    @Test
    @DisplayName("Borrar por id")
    void deleteItem() throws Exception {
        Integer idArticulo = 1;
        Mockito.doNothing().when(service).deleteById(idArticulo);
        mockMvc.perform(delete(URL + "/" + idArticulo)).andExpect(status().isOk());
        Mockito.verify(service, times(1)).deleteById(idArticulo);
    }

    //********************************** Wrong Requests ************************************

    /**
     * Comprobamos que se devuelve un mensaje con Status de ERROR 422
     * y que contiene una excepcion UnprocessableEntityException, cuando alguno de los datos
     * de la solicitud del pedido no es valida.
     * En el concreto caso realizamos una peticion de tipo POST para crear un nuevo Order,
     * que contiene una fecha no valida, por ser anterior al dia en curso.
     * Podemos modificar el dato no valido obteniendo siempre la misma respuesta.
     *
     */
/*
    @Test
    @DisplayName("Respuesta de error 422")
    void requestUnprocessableEntity() throws Exception {
        Item item= new Item("SillaConfort", "La mejor silla si estas mucho tiempo sentado", 60.5f, 60);

        MvcResult result = mockMvc.perform(
                post(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(item))
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(HttpStatus.UNPROCESSABLE_ENTITY.value()).isEqualTo(result.getResponse().getStatus());
        assertThat(UnprocessableEntityException.class).isEqualTo(result.getResolvedException().getClass());
        LOG.info(result.getResolvedException().getMessage());
        //assertThat("La fecha del pedido no puede ser anterior a hoy").isEqualTo(result.getResolvedException().getMessage());
    }
*/


}