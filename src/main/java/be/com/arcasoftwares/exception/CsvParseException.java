package be.com.arcasoftwares.exception;

public class CsvParseException extends RuntimeException {
    public CsvParseException(final String message, final Exception ex) {
        super(message, ex);
    }
}
