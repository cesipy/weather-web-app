package at.qe.skeleton.external.services;

public class ApiQueryException  extends Exception{
    public ApiQueryException(String message) {
        super(message);
    }

    public ApiQueryException() {
        super();
    }
}
