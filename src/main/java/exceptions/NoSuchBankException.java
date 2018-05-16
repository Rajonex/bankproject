package exceptions;

public class NoSuchBankException extends Throwable {
    public NoSuchBankException(String message) {
        super(message);
    }
}
