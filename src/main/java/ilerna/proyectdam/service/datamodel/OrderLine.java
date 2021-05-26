package ilerna.proyectdam.service.datamodel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
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

    //Constructors
    public OrderLine() {
    }

    public OrderLine(Float subtotal, Integer cantidad, Order pedido, Item articulo) {
        this.subtotal = subtotal;
        this.cantidad = cantidad;
        this.pedido = pedido;
        this.articulo = articulo;
    }

    //Getters y setters
    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer catidad) {
        this.cantidad = catidad;
    }

    public Order getPedido() {
        return pedido;
    }

    public void setPedido(Order pedido) {
        this.pedido = pedido;
    }

    public Item getArticulo() {
        return articulo;
    }

    public void setArticulo(Item articulo) {
        this.articulo = articulo;
    }

    public Integer getIdLine() {
        return idLine;
    }

    public void setIdLine(Integer idLine) {
        this.idLine = idLine;
    }
}

