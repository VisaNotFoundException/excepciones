package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;

public class BadRequestException extends MicroserviceException {

    public BadRequestException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

    public BadRequestException(ErrorCode errorCode, Throwable cause, Object... arguments) {
        super(errorCode, cause, arguments);
    }

    public static BadRequestException requestInvalida(String detalle) {
        return new BadRequestException(ErrorCode.APP_02_REQUEST_INVALIDA, detalle);
    }

    public static BadRequestException parametroFaltante(String nombreParametro) {
        return new BadRequestException(ErrorCode.APP_03_PARAMETRO_FALTANTE, nombreParametro);
    }

    public static BadRequestException formatoInvalido(String campo, String valor) {
        return new BadRequestException(ErrorCode.APP_04_FORMATO_INVALIDO, campo, valor);
    }
}
