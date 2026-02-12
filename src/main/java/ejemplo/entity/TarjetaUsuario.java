package ejemplo.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tarjetas_usuario")
public class TarjetaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String marca; // VISA / MASTERCARD / AMEX

    @Column(name="ultimos_4", nullable = false, length = 4)
    private String ultimos4;

    @Column(name="mes_vencimiento", nullable = false)
    private Integer mesVencimiento;

    @Column(name="anio_vencimiento", nullable = false)
    private Integer anioVencimiento;

    @Column(name="nombre_titular", nullable = false)
    private String nombreTitular;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name="es_principal", nullable = false)
    private Boolean esPrincipal = false;

    @Column(name="fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion = OffsetDateTime.now();

    public TarjetaUsuario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getUltimos4() { return ultimos4; }
    public void setUltimos4(String ultimos4) { this.ultimos4 = ultimos4; }

    public Integer getMesVencimiento() { return mesVencimiento; }
    public void setMesVencimiento(Integer mesVencimiento) { this.mesVencimiento = mesVencimiento; }

    public Integer getAnioVencimiento() { return anioVencimiento; }
    public void setAnioVencimiento(Integer anioVencimiento) { this.anioVencimiento = anioVencimiento; }

    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }

    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(OffsetDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
