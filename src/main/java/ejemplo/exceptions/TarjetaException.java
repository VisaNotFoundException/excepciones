package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;

public class TarjetaException extends MicroserviceException {

    public TarjetaException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

    public TarjetaException(ErrorCode errorCode, Throwable cause, Object... arguments) {
        super(errorCode, cause, arguments);
    }

    public static TarjetaException noEncontrada(Long id) {
        return new TarjetaException(ErrorCode.CARD_01_NO_ENCONTRADA, id);
    }

    public static TarjetaException tokenDuplicado(String token) {
        return new TarjetaException(ErrorCode.CARD_02_TOKEN_DUPLICADO, token);
    }

    public static TarjetaException numeroInvalido() {
        return new TarjetaException(ErrorCode.CARD_03_NUMERO_INVALIDO);
    }

    public static TarjetaException ultimos4Invalidos(String ultimos4) {
        return new TarjetaException(ErrorCode.CARD_04_ULTIMOS4_INVALIDO, ultimos4);
    }

    public static TarjetaException vencimientoInvalido(Integer mes, Integer anio) {
        return new TarjetaException(ErrorCode.CARD_05_VENCIMIENTO_INVALIDO, mes, anio);
    }

    public static TarjetaException noPerteneceAUsuario(Long tarjetaId, Long usuarioId) {
        return new TarjetaException(ErrorCode.CARD_06_TARJETA_NO_PERTENECE_USUARIO, tarjetaId, usuarioId);
    }
}
