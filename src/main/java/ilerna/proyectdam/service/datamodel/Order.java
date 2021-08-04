package ilerna.proyectdam.service.datamodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Jose F. Bejarano
 * @since 2020
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "pedidos")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idPedido;

    @DateTimeFormat
    @FutureOrPresent(message = "La fecha del pedido no puede ser anterior a hoy")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    @Column
    private LocalDate fecha;
    @Column
   @NotNull( message = "El pedido no tiene IVA")
    private Float ivaPedido;
    @Column
    private Float porcentajeIva;
    @Column
    @NotNull( message = "El total del pedido está vacío")
    private Float total;

    //Relacion N..1
    @JsonIgnoreProperties("listaPedidos")
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Client cliente;

    @JsonManagedReference
    //Con "CascadeType.ALL" el JPARepository no procesa el deleteById para los pedidos
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OrderLine> lineasPedido;

    public Order(LocalDate fecha, Float ivaPedido, Float porcentajeIva, Float total, Client cliente, List<OrderLine> lineasPedido) {
        this.fecha = fecha;
        this.ivaPedido = ivaPedido;
        this.porcentajeIva = porcentajeIva;
        this.total = total;
        this.cliente = cliente;
        this.lineasPedido = lineasPedido;
    }
    //Testing Constructors
    public Order(Float total, Client cliente, LocalDate fecha) {
        this.total = total;
        this.cliente = cliente;
        this.fecha = fecha;
    }
    public Order(LocalDate fecha, Float ivaPedido, Float porcentajeIva, Float total) {
        this.fecha = fecha;
        this.ivaPedido=ivaPedido;
        this.porcentajeIva = porcentajeIva;
        this.total = total;
    }
    public Order(Float total, Client cliente, List<OrderLine> lineasPedido) {
        this.total = total;
        this.cliente = cliente;
        this.lineasPedido = lineasPedido;
    }

    @Override
    public String toString() {
        return "Order{" +
                "idPedido=" + idPedido +
                ", fecha=" + fecha +
                ", ivaPedido=" + ivaPedido +
                ", porcentajeIva=" + porcentajeIva +
                ", total=" + total +
                ", Cliente=" + cliente
                + '}';
    }
}
