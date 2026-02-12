package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;

public class DatabaseException extends MicroserviceException {

    public DatabaseException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

    public DatabaseException(ErrorCode errorCode, Throwable cause, Object... arguments) {
        super(errorCode, cause, arguments);
    }

    public static DatabaseException restriccionViolada(String detalle) {
        return new DatabaseException(ErrorCode.DB_01_RESTRICCION_VIOLADA, detalle);
    }

    public static DatabaseException restriccionViolada(String detalle, Throwable cause) {
        return new DatabaseException(ErrorCode.DB_01_RESTRICCION_VIOLADA, cause, detalle);
    }

    public static DatabaseException conflictoActualizacion() {
        return new DatabaseException(ErrorCode.DB_02_CONFLICTO_ACTUALIZACION);
    }

    public static DatabaseException registroNoExiste(String detalle) {
        return new DatabaseException(ErrorCode.DB_03_REGISTRO_NO_EXISTE, detalle);
    }

    public static DatabaseException errorTransaccion(String detalle) {
        return new DatabaseException(ErrorCode.DB_04_ERROR_TRANSACCION, detalle);
    }
}
