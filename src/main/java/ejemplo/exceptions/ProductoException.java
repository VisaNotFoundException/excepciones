package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;

public class ProductoException extends MicroserviceException {

    public ProductoException(ErrorCode errorCode, Object... arguments) {
        super(errorCode, arguments);
    }

    public ProductoException(ErrorCode errorCode, Throwable cause, Object... arguments) {
        super(errorCode, cause, arguments);
    }

    public static ProductoException noEncontrado(Long id) {
        return new ProductoException(ErrorCode.PRD_01_NO_ENCONTRADO, id);
    }

    public static ProductoException skuDuplicado(String sku) {
        return new ProductoException(ErrorCode.PRD_02_SKU_DUPLICADO, sku);
    }

    public static ProductoException sinStock(Long productoId, Integer stockActual) {
        return new ProductoException(ErrorCode.PRD_03_SIN_STOCK, productoId, stockActual);
    }

    public static ProductoException cantidadInvalida(Integer cantidad) {
        return new ProductoException(ErrorCode.PRD_04_CANTIDAD_INVALIDA, cantidad);
    }

    public static ProductoException precioInvalido(Integer precioCentavos) {
        return new ProductoException(ErrorCode.PRD_05_PRECIO_INVALIDO, precioCentavos);
    }
}
