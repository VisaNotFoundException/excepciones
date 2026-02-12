package ejemplo.controller;

import ejemplo.entity.Venta;
import ejemplo.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    // DTO simple para crear venta (didáctico)
    public static class VentaCrearRequest {
        public Long usuarioId;
        public Long productoId;
        public Long tarjetaId;
        public Integer cantidad;
    }

    @PostMapping
    public ResponseEntity<Venta> crear(@RequestBody VentaCrearRequest req) {
        return ResponseEntity.ok(
                service.crear(req.usuarioId, req.productoId, req.tarjetaId, req.cantidad)
        );
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        service.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
