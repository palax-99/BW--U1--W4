package AntoninoPalazzolo.exception;

import AntoninoPalazzolo.entities.AuthorizedIssuer;

public class AvailabilityException extends RuntimeException {
    public AvailabilityException(AuthorizedIssuer authorizedIssuer) {
        super("il seguente distributore non è momentaneamente attivo: "+authorizedIssuer.getIssuerName());
    }
}
