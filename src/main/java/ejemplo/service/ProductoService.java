package ejemplo.service;

import ejemplo.entity.Producto;
import ejemplo.exceptions.ProductoException;
import ejemplo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public Producto crear(Producto producto) {
        validarProducto(producto);

        // SKU duplicado en negocio (PRD-02) en vez de DB-01
        if (repo.existsBySkuIgnoreCase(producto.getSku())) {
            throw ProductoException.skuDuplicado(producto.getSku());
        }

        producto.setId(null);
        return repo.save(producto);
    }

    public List<Producto> listarTodos() {
        return repo.findAll();
    }

    public void borrar(Long id) {
        repo.deleteById(id);
    }

    private void validarProducto(Producto p) {
        if (p == null) throw ProductoException.precioInvalido(null);

        if (p.getSku() == null || p.getSku().isBlank()) {
            throw ProductoException.skuDuplicado("sku_vacio");
        }

        if (p.getPrecioCentavos() == null || p.getPrecioCentavos() < 0) {
            throw ProductoException.precioInvalido(p.getPrecioCentavos());
        }

        if (p.getStock() != null && p.getStock() < 0) {
            throw ProductoException.cantidadInvalida(p.getStock());
        }
    }
}
