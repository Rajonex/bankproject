package exceptions;

public class NoSuchCreditException extends Throwable {
    public NoSuchCreditException(String message) {
        super(message);
    }
}
