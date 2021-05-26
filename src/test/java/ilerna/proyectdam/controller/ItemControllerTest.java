package ilerna.proyectdam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.service.datamodel.Item;
import ilerna.proyectdam.service.ItemServ;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@ActiveProfiles("test")
class ItemControllerTest {

    private static Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);

    private static final String URL = "/articulos";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemServ service;

   /* @BeforeEach
    void saveOneItem() throws SQLException {
        System.out.println("BEFORE EACH ID 6");
        repo.save(new Item(6,"Pato", "Donald", "43755589D",
                "Calle AstraZeneca", "disney@gamil.com", 928928928));
    }*/

    @Test
    @DisplayName("Testeando endpoint para crear un nuevo articulo")
    void newItem() throws Exception {

        Item item = new Item("New Balance", "Ideales para la jungla urbana", 60.5f, 60);

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
    @DisplayName("Solicitando lista de articulos")
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
    @DisplayName("Testeando borrar art por id")
    void deleteItem() throws Exception {
        Integer idArticulo = 1;
        Mockito.doNothing().when(service).deleteById(idArticulo);
        mockMvc.perform(delete(URL + "/" + idArticulo)).andExpect(status().isOk());
        Mockito.verify(service, times(1)).deleteById(idArticulo);
    }


}