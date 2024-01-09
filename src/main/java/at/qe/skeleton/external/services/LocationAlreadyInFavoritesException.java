package at.qe.skeleton.external.services;

public class LocationAlreadyInFavoritesException extends  Exception {
    public LocationAlreadyInFavoritesException() {
        super();
    }

    public LocationAlreadyInFavoritesException(String message) {
        super(message);
    }

}
