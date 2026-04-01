package AntoninoPalazzolo.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super("Elemento non trovato: " + message);
    }
}
