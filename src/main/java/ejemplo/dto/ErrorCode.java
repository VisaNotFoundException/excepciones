package ejemplo.dto;

public enum ErrorCode {

    // =========================
    // APP (Errores generales)
    // =========================
    APP_01_ERROR_INTERNO("APP-01", "Error interno del servidor"),
    APP_02_REQUEST_INVALIDA("APP-02", "Solicitud inválida: {}"),
    APP_03_PARAMETRO_FALTANTE("APP-03", "Falta el parámetro requerido: {}"),
    APP_04_FORMATO_INVALIDO("APP-04", "Formato inválido para el campo {}: {}"),

    // =========================
    // AUTH (Autenticación/Permisos)
    // =========================
    AUTH_01_NO_AUTENTICADO("AUTH-01", "Autenticación requerida o fallida"),
    AUTH_02_SIN_PERMISOS("AUTH-02", "Acceso denegado"),
    AUTH_03_SESION_INVALIDA("AUTH-03", "Sesión inválida"),

    // =========================
    // USUARIOS (Negocio)
    // =========================
    USR_01_NO_ENCONTRADO("USR-01", "Usuario no encontrado: {}"),
    USR_02_YA_REGISTRADO("USR-02", "El usuario ya está registrado: {}"),
    USR_03_EMAIL_INVALIDO("USR-03", "Formato de email inválido: {}"),

    // =========================
    // TARJETAS (Negocio)
    // =========================
    CARD_01_NO_ENCONTRADA("CARD-01", "Tarjeta no encontrada: {}"),
    CARD_02_TOKEN_DUPLICADO("CARD-02", "La tarjeta ya existe (token duplicado): {}"),
    CARD_03_NUMERO_INVALIDO("CARD-03", "Número de tarjeta inválido"),
    CARD_04_ULTIMOS4_INVALIDO("CARD-04", "Últimos 4 dígitos inválidos: {}"),
    CARD_05_VENCIMIENTO_INVALIDO("CARD-05", "Vencimiento inválido (mes/año): {}/{}"),
    CARD_06_TARJETA_NO_PERTENECE_USUARIO("CARD-06", "La tarjeta {} no pertenece al usuario {}"),

    // =========================
    // PRODUCTOS (Negocio)
    // =========================
    PRD_01_NO_ENCONTRADO("PRD-01", "Producto no encontrado: {}"),
    PRD_02_SKU_DUPLICADO("PRD-02", "SKU ya registrado: {}"),
    PRD_03_SIN_STOCK("PRD-03", "Stock insuficiente para el producto {} (stock actual: {})"),
    PRD_04_CANTIDAD_INVALIDA("PRD-04", "Cantidad inválida: {}"),
    PRD_05_PRECIO_INVALIDO("PRD-05", "Precio inválido: {}"),

    // =========================
    // VENTAS (Negocio)
    // =========================
    VTA_01_NO_ENCONTRADA("VTA-01", "Venta no encontrada: {}"),
    VTA_02_DATOS_INCOMPLETOS("VTA-02", "Datos incompletos para generar la venta"),
    VTA_03_TOTAL_INCONSISTENTE("VTA-03", "Total inconsistente (esperado: {}, recibido: {})"),
    VTA_04_ESTADO_INVALIDO("VTA-04", "Estado de venta inválido: {}"),

    // =========================
    // DB (Persistencia)
    // =========================
    DB_01_RESTRICCION_VIOLADA("DB-01", "Violación de restricción de base de datos: {}"),
    DB_02_CONFLICTO_ACTUALIZACION("DB-02", "Conflicto de actualización, refrescá y reintentá"),
    DB_03_REGISTRO_NO_EXISTE("DB-03", "No existe el registro para borrar/actualizar: {}"),
    DB_04_ERROR_TRANSACCION("DB-04", "Error de transacción en base de datos: {}"),

    // =========================
    // EXT (Servicios externos)
    // =========================
    EXT_01_SERVICIO_FALLA("EXT-01", "Error inesperado al llamar al servicio externo: {}");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
