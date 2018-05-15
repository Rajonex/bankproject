package exceptions;

public class NoSuchAccountException extends Exception {
    public NoSuchAccountException(String message) {
        super(message);
    }
}
