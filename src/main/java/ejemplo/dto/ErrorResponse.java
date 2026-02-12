package ejemplo.dto;

import java.time.OffsetDateTime;

public class ErrorResponse {

    private String errorId;
    private String code;          // USR-01, PRD-03, etc.
    private String message;       // mensaje final (formateado)
    private int httpStatus;       // 400/404/409/500
    private Integer businessStatus; // 700/701/702 opcional
    private String path;
    private OffsetDateTime timestamp;

    // info útil para debug
    private String exception;
    private String rootCause;
    private String originClass;
    private String originMethod;
    private Integer originLine;

    public ErrorResponse() {}

    // Getters/Setters
    public String getErrorId() { return errorId; }
    public void setErrorId(String errorId) { this.errorId = errorId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getHttpStatus() { return httpStatus; }
    public void setHttpStatus(int httpStatus) { this.httpStatus = httpStatus; }

    public Integer getBusinessStatus() { return businessStatus; }
    public void setBusinessStatus(Integer businessStatus) { this.businessStatus = businessStatus; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }

    public String getException() { return exception; }
    public void setException(String exception) { this.exception = exception; }

    public String getRootCause() { return rootCause; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }

    public String getOriginClass() { return originClass; }
    public void setOriginClass(String originClass) { this.originClass = originClass; }

    public String getOriginMethod() { return originMethod; }
    public void setOriginMethod(String originMethod) { this.originMethod = originMethod; }

    public Integer getOriginLine() { return originLine; }
    public void setOriginLine(Integer originLine) { this.originLine = originLine; }
}
