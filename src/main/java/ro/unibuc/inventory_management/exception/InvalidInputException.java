package ro.unibuc.inventory_management.exception;

public class InvalidInputException extends RuntimeException {

    private static final String invalidInputTemplate = "Invalid input: %s";

    public InvalidInputException(String message) {
        super(String.format(invalidInputTemplate, message));
    }

    public InvalidInputException(String message, Throwable cause) {
        super(String.format(invalidInputTemplate, message), cause);
    }
}
