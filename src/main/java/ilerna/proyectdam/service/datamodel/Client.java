package ilerna.proyectdam.service.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entidad de persistencia para la tabla clientes
 *
 * @author Jose F. Bejarano
 * @since 2020
 */

@Entity
@Table(name = "clientes")
public class Client implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer idCliente; //PK
    //Relacion 1...N
    //Con "cascade = CascadeType.ALL" el JPARepository no procesa el deleteById para los pedidos
    //En las relaciones OneToMany y ManyToMany el Fetch por defecto es LAZY, no en los otros tipos de relaciones
    @OneToMany(mappedBy = "cliente", orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("cliente")
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

    public Client(Integer idCliente, String nombre, String apellidos, String nif, String direccion, String email, Integer tlfno) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nif = nif;
        this.direccion = direccion;
        this.email = email;
        this.tlfno = tlfno;
    }

    //**********Getters & setters***************
    public List<Order> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Order> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTlfno() {
        return tlfno;
    }

    public void setTlfno(Integer tlfno) {
        this.tlfno = tlfno;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
