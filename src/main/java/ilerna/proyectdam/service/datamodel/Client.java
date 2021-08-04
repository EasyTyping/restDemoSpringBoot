package ilerna.proyectdam.service.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entidad de persistencia para la tabla clientes
 * @author Jose F. Bejarano
 * @since 2020
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "clientes")
public class Client implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTS_SEQ")
    @SequenceGenerator(name="CLIENTS_SEQ", initialValue = 6)
    private Integer idCliente; //PK
    //Relacion 1...N
    //Con "cascade = CascadeType.ALL" el JPARepository no procesa el deleteById para los pedidos
    //En las relaciones OneToMany y ManyToMany el Fetch por defecto es LAZY, no en los otros tipos de relaciones
    @OneToMany(mappedBy = "cliente", orphanRemoval = true, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
  //  @JsonIgnoreProperties("cliente")
    @JsonIgnore
    private List<Order> listaPedidos;


    @Column(name = "nombre", nullable = false)
    @NotBlank( message = "El nombre es obligatorio")
    @Size(min = 3, max = 40, message = "El tamaño del campo nombre no cumple los requisitos, min=3, max=40")
    private String nombre;
    @Column(name = "apellidos", length = 50)
    @Size(max = 50, message = "El tamaño del campo apellidos no cumple los requisitos, max=50")
    private String apellidos;
    @Column(name = "nif", length = 9, unique = true)
   // @Pattern(regexp = "^[0-9]{8}[-]?[A-Z]{1}$", message = "Formato incorrecto de DNI")
    @Pattern(regexp = "^[a-zA-Z0-9]{1}\\d{7}[a-zA-Z]{1}$", message = "Formato incorrecto de NIF")
    @NotBlank(message = "El NIF es obligatorio")
    private String nif;
    @Column(name = "direccion", length = 50)
    private String direccion;
    @Column(name = "email", length = 50, unique = true, nullable = false)
    @Pattern(regexp = "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$", message = "El Email no cumple con el formato o está vacio")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @Column(name = "tlfno", length = 11)
    private Integer tlfno;

    public Client() { }

    //Constructores para el testing
    public Client(String nombre, String apellidos, String nif, String direccion,
                  String email, Integer tlfno) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
        this.direccion = direccion;
        this.email = email;
        this.tlfno = tlfno;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idCliente=" + idCliente +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", nif='" + nif + '\'' +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                ", tlfno=" + tlfno +
                '}';
    }

}
