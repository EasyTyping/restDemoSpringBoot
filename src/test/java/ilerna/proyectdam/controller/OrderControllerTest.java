package ilerna.proyectdam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ilerna.proyectdam.ProyectoFinalApplication;
import ilerna.proyectdam.exceptions.MyNotFoundException;
import ilerna.proyectdam.exceptions.UnprocessableEntityException;
import ilerna.proyectdam.service.datamodel.Item;
import ilerna.proyectdam.service.datamodel.Order;
import ilerna.proyectdam.repository.OrderRepo;
import ilerna.proyectdam.service.OrderServ;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@DisplayName("Testeando endpoints en OrderController")
class OrderControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProyectoFinalApplication.class);
    private static final String URL = "/pedidos";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    OrderRepo repo;
    @MockBean
    OrderServ service;

    @Test
    @DisplayName("Crear nuevo pedido")
    void newOrder() throws Exception {

        Order order = new Order(LocalDate.now(), 23.4f, 19f, 200.8f);

        MvcResult result = mockMvc.perform(
                post(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(order))
        ).andExpect(status().isOk()).andReturn();

        assertThat("El pedido se ha creado correctamente").isEqualTo(result.getResponse().getContentAsString());

        LOG.info(result.getResponse().getContentAsString());
        //    LOG.info(String.valueOf(result.getResolvedException()));
        LOG.info(String.valueOf(result.getResponse().getStatus()));
    }

    @Test
    @DisplayName("Consultar lista de pedidos")
    void getOrderList() throws Exception {

        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order(LocalDate.now(), 23.4f, 19f, 200.8f));
        orderList.add(new Order(LocalDate.now(), 23.4f, 20f, 350.7f));
        orderList.add(new Order(LocalDate.now(), 23.4f, 21f, 405.6f));


        Mockito.when(service.findAll()).thenReturn(orderList);

        //  RequestBuilder request = MockMvcRequestBuilders.get(URL);
        MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isOk()).andReturn();
        String actualResponse = result.getResponse().getContentAsString();
        LOG.info(actualResponse);

        String expectedJsonResponse = mapper.writeValueAsString(orderList);
        LOG.info(expectedJsonResponse);

        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
        LOG.info("Status de la Respuesta: " + result.getResponse().getStatus());
    }

    @Test
    @DisplayName("Consultar art por su id")
    void getOrderById() throws Exception {

        Order order = new Order(LocalDate.now(), 23.4f, 19f, 200.8f);
        Integer id=1;

        Mockito.when(service.findById(id)).thenReturn(java.util.Optional.of(order));

        //  RequestBuilder request = MockMvcRequestBuilders.get(URL);
        MvcResult result = mockMvc.perform(get(URL+"/"+ id)).andExpect(status().isOk()).andReturn();
        String actualResponse = result.getResponse().getContentAsString();
        LOG.info(actualResponse);

        String expectedJsonResponse = mapper.writeValueAsString(java.util.Optional.of(order));
        LOG.info(expectedJsonResponse);

        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);
        LOG.info("Status de la Respuesta: " + result.getResponse().getStatus());
        LOG.info("Se ha obtenido el articulo con id: "+ id);
    }


    @Test
    @DisplayName("Borrar pedido por su id")
    void deleteOrder() throws Exception {
        repo.save(new Order(LocalDate.now(), 23.4f, 21f, 3567.5f));
        Integer idOrder = 1;
        Mockito.doNothing().when(service).deleteById(idOrder);
        mockMvc.perform(delete(URL + "/" + idOrder)).andExpect(status().isOk());
        Mockito.verify(service, times(1)).deleteById(idOrder);
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
    @Test
    @DisplayName("Respuesta de error 422")
    void requestUnprocessableEntity() throws Exception {
        Order order = new Order(LocalDate.of(1996, 03, 12), 23.4f, 19f, 200.8f);

        MvcResult result = mockMvc.perform(
                post(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(order))
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(HttpStatus.UNPROCESSABLE_ENTITY.value()).isEqualTo(result.getResponse().getStatus());
        assertThat(UnprocessableEntityException.class).isEqualTo(result.getResolvedException().getClass());
        assertThat("La fecha del pedido no puede ser anterior a hoy").isEqualTo(result.getResolvedException().getMessage());
    }

    /**
     * Comprobamos que se devuelve un mensaje con Status de ERROR 404
     * y que contiene una excepcion MyNotFoundException, cuando el pedido no existe.
     */
    @Test
    @DisplayName("Respuesta de error 404")
    void requestMyNotFoundException() throws Exception {
        int idOrder = 2;

        MvcResult result = mockMvc.perform(
                get(URL + "/" + idOrder)
        ).andExpect(status().is4xxClientError()).andReturn();

        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(result.getResponse().getStatus());
        assertThat(MyNotFoundException.class).isEqualTo(result.getResolvedException().getClass());
        assertThat("No se encuentra el pedido con id " + idOrder).isEqualTo(result.getResolvedException().getMessage());
    }
}