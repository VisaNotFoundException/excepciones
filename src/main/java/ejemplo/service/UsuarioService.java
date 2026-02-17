package ejemplo.service;

import ejemplo.dto.Roles;
import ejemplo.entity.Usuario;
import ejemplo.exceptions.UsuarioException;
import ejemplo.repository.UsuarioRepository;
import ejemplo.repository.RolesDeUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final RolesDeUsuarioRepository rolesRepo;

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public UsuarioService(UsuarioRepository repo, RolesDeUsuarioRepository rolesRepo) {
        this.repo = repo;
        this.rolesRepo = rolesRepo;
    }

    public Usuario crear(Usuario usuario) {
        if (usuario == null) throw UsuarioException.emailInvalido("null");

        String email = usuario.getEmail();
        if (email == null || email.isBlank() || !EMAIL.matcher(email).matches()) {
            throw UsuarioException.emailInvalido(email);
        }

        if (repo.existsByEmailIgnoreCase(email)) {
            throw UsuarioException.yaRegistrado(email);
        }

        usuario.setId(null);

        if (usuario.getRol() == null) {
            var rolDefault = rolesRepo.findByNombre(Roles.USUARIO)
                    .orElseThrow(() -> new IllegalStateException("No existe el rol USUARIO en roles_de_usuario"));
            usuario.setRol(rolDefault);
        }

        return repo.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return repo.findAll();
    }

    public void borrar(Long id) {
        repo.deleteById(id);
    }
}
