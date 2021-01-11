package engine.task;

public class InputError {
    public static final int UNKNOWN_COMMAND = 1;
    public static final int NOT_AVAILABLE = 2;
    public static final int INVALID_NODE = 3;
    public static final int INVALID_TRIANGLE = 4;
    public static final int CANT_BE_APPLIED = 5;
    public static final int CANT_ADD_PATH = 6;
    public static final int POSITIVE_AMOUNT = 7;
    public static final int NOT_NEAR_TRIANGLE = 8;

    private String message;
    private int errorCode;
    private boolean critical;

    public InputError(String message, int errorCode, boolean critical) {
        this.message = message;
        this.errorCode = errorCode;
        this.critical = critical;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public boolean isCritical() {
        return critical;
    }
}
