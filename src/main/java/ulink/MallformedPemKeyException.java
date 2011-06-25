package ulink;

/**
 *
 */
public class MallformedPemKeyException extends Exception{

    public MallformedPemKeyException(Throwable cause) {
        super(cause);
    }

    public MallformedPemKeyException(String message) {
        super(message);
    }
}
