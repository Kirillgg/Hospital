package hospital.database;

/**
 * Error class error type foreign key constraint
 * @author Kirill
 */
public class ForeignKeyError extends Error {

    public ForeignKeyError() {
    }

    public ForeignKeyError(String message) {
        super(message);
    }

}
