package ejemplo.controller;

import ejemplo.entity.TarjetaUsuario;
import ejemplo.service.TarjetaUsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaUsuarioController {

    private final TarjetaUsuarioService service;

    public TarjetaUsuarioController(TarjetaUsuarioService service) {
        this.service = service;
    }

    // Crear tarjeta para un usuario
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<TarjetaUsuario> crear(
            @PathVariable Long usuarioId,
            @RequestBody TarjetaUsuario tarjeta
    ) {
        return ResponseEntity.ok(service.crear(usuarioId, tarjeta));
    }

    @GetMapping
    public ResponseEntity<List<TarjetaUsuario>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        service.borrar(id);
        return ResponseEntity.noContent().build();
    }
}
