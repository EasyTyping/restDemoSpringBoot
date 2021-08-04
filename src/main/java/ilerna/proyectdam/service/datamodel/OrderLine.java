package ilerna.proyectdam.service.datamodel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "lineasPedido")
public class OrderLine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idLine;
    @Column
    private Float subtotal;
    @Column
    private Integer cantidad;
    //Relaciones
    @JsonBackReference
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPedido")
    private Order pedido;
    @ManyToOne
    @JoinColumn(name = "idArticulo")
    @JsonIgnoreProperties("lineas")
    private Item articulo;

    public OrderLine(Float subtotal, Integer cantidad, Order pedido, Item articulo) {
        this.subtotal = subtotal;
        this.cantidad = cantidad;
        this.pedido = pedido;
        this.articulo = articulo;
    }

}