package ejemplo.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tarjeta_id", nullable = false)
    private TarjetaUsuario tarjeta;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name="precio_unitario_cent", nullable = false)
    private Integer precioUnitarioCent;

    @Column(name="total_centavos", nullable = false)
    private Integer totalCentavos;

    @Column(nullable = false)
    private String estado = "PAGADA"; // PAGADA / PENDIENTE / CANCELADA

    @Column(name="fecha_compra", nullable = false)
    private OffsetDateTime fechaCompra = OffsetDateTime.now();

    public Venta() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public TarjetaUsuario getTarjeta() { return tarjeta; }
    public void setTarjeta(TarjetaUsuario tarjeta) { this.tarjeta = tarjeta; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Integer getPrecioUnitarioCent() { return precioUnitarioCent; }
    public void setPrecioUnitarioCent(Integer precioUnitarioCent) { this.precioUnitarioCent = precioUnitarioCent; }

    public Integer getTotalCentavos() { return totalCentavos; }
    public void setTotalCentavos(Integer totalCentavos) { this.totalCentavos = totalCentavos; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public OffsetDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(OffsetDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
}
