package ilerna.proyectdam.service.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
/**
 * Entidad de persistencia para la tabla de Articulos
 *
 * @author Jose F. Bejarano
 * @since 2020
 */

@Entity
@Table(name = "articulos")
public class Item implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idArticulo;

    @Column(length = 50)
    @NotBlank( message = "El nombre del articulo es obligatorio")
   //@Pattern(regexp = "", message = "No se permiten caracteres especiales")
    private String nombreArticulo;
    @Column
    private String descripcion;
    @Column(precision = 11, scale = 2)
    @NotNull( message = "El articulo debe tener un precio por unidad")
    private Float precioUnidad;
    @Column(precision = 5)
    @NotNull(message = "El stock no puede estar vacio, introduzca 0 unidades")
    private Integer stock;


    @JsonIgnoreProperties("articulo")
    @OneToMany(mappedBy = "articulo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderLine> lineas;


//************Constructores****************

    public Item() {
    }

    public Item(String nombreArticulo, String descripcion, Float precioUnidad, Integer stock) {
        this.nombreArticulo = nombreArticulo;
        this.descripcion = descripcion;
        this.precioUnidad = precioUnidad;
        this.stock = stock;
    }

    //**********Getters & setters***************


    public List<OrderLine> getLineas() {
        return lineas;
    }

    public void setLineas(List<OrderLine> lineas) {
        this.lineas = lineas;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(Float precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

}

