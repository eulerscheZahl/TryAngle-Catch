package engine.task;

public class InputError {
    private String message;
    private boolean critical;

    public InputError(String message, boolean critical) {
        this.message = message;
        this.critical = critical;
    }

    public String getMessage() {
        return message;
    }

    public boolean isCritical() {
        return critical;
    }
}
