package ejemplo.service;

import ejemplo.entity.Producto;
import ejemplo.entity.TarjetaUsuario;
import ejemplo.entity.Usuario;
import ejemplo.entity.Venta;
import ejemplo.exceptions.ProductoException;
import ejemplo.exceptions.TarjetaException;
import ejemplo.exceptions.UsuarioException;
import ejemplo.exceptions.VentaException;
import ejemplo.repository.ProductoRepository;
import ejemplo.repository.TarjetaUsuarioRepository;
import ejemplo.repository.UsuarioRepository;
import ejemplo.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;
    private final TarjetaUsuarioRepository tarjetaRepo;

    public VentaService(
            VentaRepository ventaRepo,
            UsuarioRepository usuarioRepo,
            ProductoRepository productoRepo,
            TarjetaUsuarioRepository tarjetaRepo
    ) {
        this.ventaRepo = ventaRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
        this.tarjetaRepo = tarjetaRepo;
    }

    public Venta crear(Long usuarioId, Long productoId, Long tarjetaId, Integer cantidad) {
        validarRequestVenta(usuarioId, productoId, tarjetaId, cantidad);

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> UsuarioException.noEncontrado(usuarioId));

        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> ProductoException.noEncontrado(productoId));

        TarjetaUsuario tarjeta = tarjetaRepo.findById(tarjetaId)
                .orElseThrow(() -> TarjetaException.noEncontrada(tarjetaId));

        // Regla de negocio: la tarjeta debe pertenecer al usuario
        if (!tarjeta.getUsuario().getId().equals(usuario.getId())) {
            throw TarjetaException.noPerteneceAUsuario(tarjetaId, usuarioId);
        }

        // Reglas producto/cantidad
        if (cantidad <= 0) throw ProductoException.cantidadInvalida(cantidad);

        Integer stock = producto.getStock();
        if (stock != null && stock < cantidad) {
            throw ProductoException.sinStock(producto.getId(), stock);
        }

        // Calcular
        int precio = producto.getPrecioCentavos();
        int total = precio * cantidad;

        Venta v = new Venta();
        v.setUsuario(usuario);
        v.setProducto(producto);
        v.setTarjeta(tarjeta);
        v.setCantidad(cantidad);
        v.setPrecioUnitarioCent(precio);
        v.setTotalCentavos(total);
        v.setEstado("PAGADA");

        // Opcional: descontar stock en memoria (didáctico)
        if (stock != null) producto.setStock(stock - cantidad);

        return ventaRepo.save(v);
    }

    public List<Venta> listarTodas() {
        return ventaRepo.findAll();
    }

    public void borrar(Long id) {
        ventaRepo.deleteById(id);
    }

    private void validarRequestVenta(Long usuarioId, Long productoId, Long tarjetaId, Integer cantidad) {
//        if (usuarioId == null || productoId == null || tarjetaId == null || cantidad == null) {
//            throw VentaException.datosIncompletos();
//        }
    }
}
