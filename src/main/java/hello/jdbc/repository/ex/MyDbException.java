package hello.jdbc.repository.ex;

//언체크 예외임 (RuntimeException 상속)
public class MyDbException extends RuntimeException {
    public MyDbException() {
    }

    public MyDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDbException(String message) {
        super(message);
    }

    public MyDbException(Throwable cause) {
        super(cause);
    }
}
