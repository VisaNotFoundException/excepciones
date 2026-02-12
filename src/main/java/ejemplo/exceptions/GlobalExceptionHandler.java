package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;
import ejemplo.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    // =========================
    // HANDLERS (bien cortitos)
    // =========================

    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<ErrorResponse> usuario(UsuarioException ex, HttpServletRequest req) { return responder(ex, req); }

    @ExceptionHandler(TarjetaException.class)
    public ResponseEntity<ErrorResponse> tarjeta(TarjetaException ex, HttpServletRequest req) { return responder(ex, req); }

//    @ExceptionHandler(ProductoException.class)
//    public ResponseEntity<ErrorResponse> producto(ProductoException ex, HttpServletRequest req) { return responder(ex, req); }

    @ExceptionHandler(VentaException.class)
    public ResponseEntity<ErrorResponse> venta(VentaException ex, HttpServletRequest req) { return responder(ex, req); }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequest(BadRequestException ex, HttpServletRequest req) { return responder(ex, req); }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> database(DatabaseException ex, HttpServletRequest req) { return responder(ex, req); }

    /** Fallback para excepción de negocio NUEVA (no controlada explícitamente) */
    @ExceptionHandler(MicroserviceException.class)
    public ResponseEntity<ErrorResponse> negocioNuevaNoControlada(MicroserviceException ex, HttpServletRequest req) {
        return responderNegocioNoControlada(ex, req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        return responder(BadRequestException.requestInvalida("Validación fallida"), req);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> constraint(DataIntegrityViolationException ex, HttpServletRequest req) {
        return responder(DatabaseException.restriccionViolada(rootMessage(ex), ex), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> desconocido(Exception ex, HttpServletRequest req) {
        return responder(new MicroserviceException(ErrorCode.APP_01_ERROR_INTERNO, rootMessage(ex)), req);
    }

    // =========================
    // RESPONDER (oculta info)
    // =========================

    private ResponseEntity<ErrorResponse> responder(MicroserviceException ex, HttpServletRequest req) {
        HttpStatus http = mapHttpStatus(ex.getErrorCode());
        Integer biz = mapBusinessStatus(ex.getErrorCode());
        return ResponseEntity.status(http).body(buildError(ex, req, http, biz, false));
    }

    private ResponseEntity<ErrorResponse> responderNegocioNoControlada(MicroserviceException ex, HttpServletRequest req) {
        HttpStatus http = mapHttpStatus(ex.getErrorCode());
        Integer biz = mapBusinessStatus(ex.getErrorCode());
        return ResponseEntity.status(http).body(buildError(ex, req, http, biz, true));
    }

    // =========================
    // STORY MODE: buildError()
    // =========================

    private ErrorResponse buildError(
            MicroserviceException ex,
            HttpServletRequest req,
            HttpStatus httpStatus,
            Integer businessStatus,
            boolean negocioNoControlada
    ) {
        String errorId = generarErrorId();
        OffsetDateTime ahora = ahora();
        Throwable causaRaiz = obtenerCausaRaiz(ex);
        StackTraceElement origen = buscarOrigen(ex);
        String mensaje = armarMensaje(ex, negocioNoControlada);

        ErrorResponse response = armarResponse(ex, req, httpStatus, businessStatus, errorId, ahora, causaRaiz, origen, mensaje);
        String logLine = construirLog(req, httpStatus, businessStatus, ex, errorId, ahora, causaRaiz, origen, mensaje, negocioNoControlada);

        imprimirLogCorto(logLine, httpStatus);
        return response;
    }

    // =========================
    // PASOS (buildError helpers)
    // =========================

    private String generarErrorId() { return UUID.randomUUID().toString(); }

    private OffsetDateTime ahora() { return OffsetDateTime.now(); }

    private Throwable obtenerCausaRaiz(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null && cur.getCause() != cur) cur = cur.getCause();
        return cur;
    }

    private StackTraceElement buscarOrigen(Throwable t) {
        for (StackTraceElement el : t.getStackTrace()) {
            if (!esInfra(el.getClassName())) return el;
        }
        return t.getStackTrace().length > 0 ? t.getStackTrace()[0] : null;
    }

    private boolean esInfra(String cn) {
        return cn.startsWith("java.")
                || cn.startsWith("jakarta.")
                || cn.startsWith("org.springframework.")
                || cn.startsWith("org.hibernate.")
                || cn.startsWith("com.fasterxml.");
    }

    private String armarMensaje(MicroserviceException ex, boolean negocioNoControlada) {
        String base = formatear(ex.getErrorCode().getMessage(), ex.getArguments());
        if (!negocioNoControlada) return base;

        return base + " | Excepción de negocio no controlada: agregá un handler específico para "
                + ex.getClass().getSimpleName();
    }

    private ErrorResponse armarResponse(
            MicroserviceException ex,
            HttpServletRequest req,
            HttpStatus httpStatus,
            Integer businessStatus,
            String errorId,
            OffsetDateTime ahora,
            Throwable causaRaiz,
            StackTraceElement origen,
            String mensaje
    ) {
        ErrorResponse body = new ErrorResponse();

        body.setErrorId(errorId);
        body.setTimestamp(ahora);
        body.setPath(req.getRequestURI());

        body.setHttpStatus(httpStatus.value());
        body.setBusinessStatus(businessStatus);

        body.setCode(ex.getErrorCode().getCode());
        body.setMessage(mensaje);

        body.setException(ex.getClass().getSimpleName());

        // ✅ rootCause "humano": si la causa raíz es la misma excepción, usamos el mensaje ya formateado
        String rootMsg = (causaRaiz == ex) ? mensaje : safe(causaRaiz.getMessage());
        body.setRootCause(causaRaiz.getClass().getSimpleName() + ": " + rootMsg);

        if (origen != null) {
            body.setOriginClass(origen.getClassName());
            body.setOriginMethod(origen.getMethodName());
            body.setOriginLine(origen.getLineNumber());
        }

        return body;
    }

    /**
     * Log en una sola línea (ideal para buscar por ERROR_ID).
     */
    private String construirLog(
            HttpServletRequest req,
            HttpStatus httpStatus,
            Integer businessStatus,
            MicroserviceException ex,
            String errorId,
            OffsetDateTime ahora,
            Throwable causaRaiz,
            StackTraceElement origen,
            String mensaje,
            boolean negocioNoControlada
    ) {
        String origin = (origen == null)
                ? "-"
                : (origen.getClassName() + "." + origen.getMethodName() + ":" + origen.getLineNumber());

        String stackCorto = miniStack(causaRaiz, 6);

        String rootMsg = (causaRaiz == ex) ? mensaje : safe(causaRaiz.getMessage());

        return "ERROR_ID=" + errorId
                + " TIME=" + ahora
                + " PATH=" + req.getMethod() + " " + req.getRequestURI()
                + " HTTP=" + httpStatus.value()
                + " BIZ=" + businessStatus
                + " CODE=" + ex.getErrorCode().getCode()
                + " EX=" + ex.getClass().getSimpleName()
                + " ROOT=" + causaRaiz.getClass().getSimpleName() + ":" + rootMsg
                + " ORIGIN=" + origin
                + " STACK=" + stackCorto
                + " NO_CONTROLADA=" + negocioNoControlada
                + " MSG=\"" + mensaje + "\"";
    }

    /**
     * Mini stacktrace (solo frames "de tu app", sin infra).
     */
    private String miniStack(Throwable t, int maxFrames) {
        StackTraceElement[] st = t.getStackTrace();
        if (st == null || st.length == 0) return "-";

        StringBuilder sb = new StringBuilder();
        int agregados = 0;

        for (StackTraceElement el : st) {
            String cn = el.getClassName();

            // 1) Solo cosas del microservicio (ajustá el prefijo si tu paquete base es otro)
            if (!cn.startsWith("ejemplo.")) continue;

            // 2) Saltar factories de exceptions (opcional, pero queda más "service->controller")
            if (cn.startsWith("ejemplo.exceptions.")) continue;

            // 3) Saltar lambdas (hace el stack más legible)
            if (el.getMethodName().startsWith("lambda$")) continue;

            if (agregados > 0) sb.append(" <- ");
            sb.append(formatoFrame(el));
            agregados++;

            if (agregados >= maxFrames) break;
        }

        return agregados == 0 ? "-" : sb.toString();
    }

    private String formatoFrame(StackTraceElement el) {
        String simpleClass = el.getClassName();
        int idx = simpleClass.lastIndexOf('.');
        if (idx >= 0) simpleClass = simpleClass.substring(idx + 1);
        return simpleClass + "." + el.getMethodName() + ":" + el.getLineNumber();
    }

    /**
     * Solo log corto (sin stacktrace).
     * Nivel: WARN para 4xx, ERROR para 5xx.
     */
    private void imprimirLogCorto(String logLine, HttpStatus httpStatus) {
        if (httpStatus.is5xxServerError()) log.error(logLine);
        else log.warn(logLine);
    }

    // =========================
    // MAPEOS (ocultos, finitos)
    // =========================

    private HttpStatus mapHttpStatus(ErrorCode code) {
        String c = code.getCode();

        if (c.startsWith("AUTH-01") || c.startsWith("AUTH-03")) return HttpStatus.UNAUTHORIZED;
        if (c.startsWith("AUTH-02")) return HttpStatus.FORBIDDEN;

        if (c.startsWith("USR-01") || c.startsWith("PRD-01") || c.startsWith("CARD-01") || c.startsWith("VTA-01"))
            return HttpStatus.NOT_FOUND;

        if (c.startsWith("USR-02") || c.startsWith("PRD-02") || c.startsWith("CARD-02") || c.startsWith("DB-01"))
            return HttpStatus.CONFLICT;

        if (c.startsWith("APP-02") || c.startsWith("APP-03") || c.startsWith("APP-04")) return HttpStatus.BAD_REQUEST;

        if (c.startsWith("USR-03") ||
                c.startsWith("CARD-03") || c.startsWith("CARD-04") || c.startsWith("CARD-05") || c.startsWith("CARD-06") ||
                c.startsWith("PRD-03") || c.startsWith("PRD-04") || c.startsWith("PRD-05") ||
                c.startsWith("VTA-02") || c.startsWith("VTA-03") || c.startsWith("VTA-04"))
            return HttpStatus.BAD_REQUEST;

        if (c.startsWith("EXT-")) return HttpStatus.BAD_GATEWAY;
        if (c.startsWith("DB-02") || c.startsWith("DB-04")) return HttpStatus.INTERNAL_SERVER_ERROR;

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private Integer mapBusinessStatus(ErrorCode code) {
        String c = code.getCode();

        if (c.startsWith("APP-02") || c.startsWith("APP-03") || c.startsWith("APP-04")) return 700;
        if (c.startsWith("USR-01") || c.startsWith("PRD-01") || c.startsWith("CARD-01") || c.startsWith("VTA-01")) return 701;
        if (c.startsWith("USR-02") || c.startsWith("PRD-02") || c.startsWith("CARD-02") || c.startsWith("DB-01")) return 702;
        if (c.startsWith("PRD-03")) return 703;
        if (c.startsWith("AUTH-01") || c.startsWith("AUTH-03")) return 705;
        if (c.startsWith("AUTH-02")) return 706;
        if (c.startsWith("EXT-")) return 790;
        return 799;
    }

    // =========================
    // UTILITARIOS EXTRA
    // =========================

    private String rootMessage(Throwable t) {
        return safe(obtenerCausaRaiz(t).getMessage());
    }

    private String safe(String s) { return s == null ? "" : s; }

    private String formatear(String template, Object[] args) {
        if (template == null) return "";
        if (args == null || args.length == 0) return template;

        String msg = template;
        for (Object a : args) msg = msg.replaceFirst("\\{\\}", Objects.toString(a));
        return msg;
    }
}
