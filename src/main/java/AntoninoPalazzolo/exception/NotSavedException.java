package AntoninoPalazzolo.exception;

public class NotSavedException extends RuntimeException {
    public NotSavedException(Object obj) {
        super("L'oggetto " + obj + " non è stato salvato.");
    }
}
