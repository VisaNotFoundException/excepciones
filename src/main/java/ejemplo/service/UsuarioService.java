package ejemplo.service;

import ejemplo.entity.Usuario;
import ejemplo.exceptions.UsuarioException;
import ejemplo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Usuario crear(Usuario usuario) {
        if (usuario == null) throw UsuarioException.emailInvalido("null");

        String email = usuario.getEmail();
        if (email == null || email.isBlank() || !EMAIL.matcher(email).matches()) {
            throw UsuarioException.emailInvalido(email);
        }

        // Para detectar duplicado en negocio (USR-02) en vez de DB-01
        // OJO: igual podría haber race condition, DB sigue siendo autoridad final.
        if (repo.existsByEmailIgnoreCase(email)) {
            throw UsuarioException.yaRegistrado(email);
        }

        usuario.setId(null);
        return repo.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repo.findAll();
    }

    public void borrar(Long id) {
        repo.deleteById(id);
    }
}
