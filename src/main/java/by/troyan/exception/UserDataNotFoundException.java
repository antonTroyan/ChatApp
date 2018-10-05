package by.troyan.exception;

public class UserDataNotFoundException extends RuntimeException {

    public UserDataNotFoundException() {
        super();
    }

    public UserDataNotFoundException(String message) {
        super(message);
    }

    public UserDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDataNotFoundException(Throwable cause) {
        super(cause);
    }
}
