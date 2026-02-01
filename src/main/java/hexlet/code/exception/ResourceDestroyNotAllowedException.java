package hexlet.code.exception;

public class ResourceDestroyNotAllowedException extends RuntimeException {
    public ResourceDestroyNotAllowedException(String message) {
        super(message);
    }
}
