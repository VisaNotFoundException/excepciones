package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;

public class VentaException extends MicroserviceException {

    public VentaException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

    public VentaException(ErrorCode errorCode, Throwable cause, Object... arguments) {
        super(errorCode, cause, arguments);
    }

    public static VentaException noEncontrada(Long id) {
        return new VentaException(ErrorCode.VTA_01_NO_ENCONTRADA, id);
    }

    public static VentaException datosIncompletos() {
        return new VentaException(ErrorCode.VTA_02_DATOS_INCOMPLETOS);
    }

    public static VentaException totalInconsistente(Integer esperado, Integer recibido) {
        return new VentaException(ErrorCode.VTA_03_TOTAL_INCONSISTENTE, esperado, recibido);
    }

    public static VentaException estadoInvalido(String estado) {
        return new VentaException(ErrorCode.VTA_04_ESTADO_INVALIDO, estado);
    }
}
