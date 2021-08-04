package ilerna.proyectdam.service.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Jose F. Bejarano
 * @since 2020
 */

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "articulos")
public class Item implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_SEQ")
    @SequenceGenerator(name = "ITEMS_SEQ", initialValue = 8)
    private Integer idArticulo;

    @Column(length = 50)
    @NotBlank(message = "El nombre del articulo es obligatorio")
    private String nombreArticulo;
    @Column
    private String descripcion;
    @Column(precision = 11, scale = 2)
    @NotNull(message = "El articulo debe tener un precio por unidad")
    private Float precioUnidad;
    @Column(precision = 5)
    @NotNull(message = "El stock no puede estar vacio, introduzca 0 unidades")
    private Integer stock;

    // @JsonIgnoreProperties("articulo")
    @JsonIgnore
    @OneToMany(mappedBy = "articulo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderLine> lineas;

    //Testing Constructor
    public Item(String nombreArticulo, String descripcion, Float precioUnidad, Integer stock) {
        this.nombreArticulo = nombreArticulo;
        this.descripcion = descripcion;
        this.precioUnidad = precioUnidad;
        this.stock = stock;
    }


}

