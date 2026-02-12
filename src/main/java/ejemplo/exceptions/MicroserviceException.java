package ejemplo.exceptions;

import ejemplo.dto.ErrorCode;

public class MicroserviceException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object[] arguments;

    public MicroserviceException(ErrorCode errorCode, Object... arguments) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.arguments = arguments;
    }

    public MicroserviceException(ErrorCode errorCode, Throwable cause, Object... arguments) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.arguments = arguments;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
