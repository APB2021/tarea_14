package dao;

/**
 * Excepción personalizada para errores en la capa de acceso a datos.
 */
public class DataAccessException extends RuntimeException {

	private static final long serialVersionUID = 8930860227685443427L;

	public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
