package at.qe.skeleton.external.controllers;

public class EmptyLocationException extends Exception{

    public EmptyLocationException(String message) {
        super(message);
    }

    public EmptyLocationException() {
        super();
    }
}
