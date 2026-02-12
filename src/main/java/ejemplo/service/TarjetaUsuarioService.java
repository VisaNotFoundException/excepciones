package ejemplo.service;

import ejemplo.entity.TarjetaUsuario;
import ejemplo.entity.Usuario;
import ejemplo.exceptions.TarjetaException;
import ejemplo.exceptions.UsuarioException;
import ejemplo.repository.TarjetaUsuarioRepository;
import ejemplo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarjetaUsuarioService {

    private final TarjetaUsuarioRepository repo;
    private final UsuarioRepository usuarioRepo;

    public TarjetaUsuarioService(TarjetaUsuarioRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
    }

    public TarjetaUsuario crear(Long usuarioId, TarjetaUsuario tarjeta) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> UsuarioException.noEncontrado(usuarioId));

        validarTarjeta(tarjeta);

        // Token duplicado en negocio (CARD-02) en vez de DB-01
        if (repo.existsByToken(tarjeta.getToken())) {
            throw TarjetaException.tokenDuplicado(tarjeta.getToken());
        }

        tarjeta.setId(null);
        tarjeta.setUsuario(usuario);
        return repo.save(tarjeta);
    }

    public List<TarjetaUsuario> listarTodas() {
        return repo.findAll();
    }

    public void borrar(Long id) {
        repo.deleteById(id);
    }

    private void validarTarjeta(TarjetaUsuario tarjeta) {
        if (tarjeta == null) throw TarjetaException.numeroInvalido();

        String ult4 = tarjeta.getUltimos4();
        if (ult4 == null || !ult4.matches("^[0-9]{4}$")) {
            throw TarjetaException.ultimos4Invalidos(ult4);
        }

        Integer mes = tarjeta.getMesVencimiento();
        Integer anio = tarjeta.getAnioVencimiento();
        if (mes == null || anio == null || mes < 1 || mes > 12 || anio < 2024 || anio > 2099) {
            throw TarjetaException.vencimientoInvalido(mes, anio);
        }

        String token = tarjeta.getToken();
        if (token == null || token.isBlank()) {
            throw TarjetaException.tokenDuplicado("token_vacio");
        }
    }
}
