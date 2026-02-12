package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;

public class UsuarioException extends MicroserviceException {

    public UsuarioException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

    public UsuarioException(ErrorCode errorCode, Throwable cause, Object... arguments) {
        super(errorCode, cause, arguments);
    }

    public static UsuarioException noEncontrado(Long id) {
        return new UsuarioException(ErrorCode.USR_01_NO_ENCONTRADO, id);
    }

    public static UsuarioException yaRegistrado(String email) {
        return new UsuarioException(ErrorCode.USR_02_YA_REGISTRADO, email);
    }

    public static UsuarioException emailInvalido(String email) {
        return new UsuarioException(ErrorCode.USR_03_EMAIL_INVALIDO, email);
    }
}
