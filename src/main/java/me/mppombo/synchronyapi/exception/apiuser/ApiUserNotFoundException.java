package me.mppombo.synchronyapi.exception.apiuser;

public class ApiUserNotFoundException extends RuntimeException {
    private final Long requestedId;

    public ApiUserNotFoundException(Long id) {
        super("No user found with ID=" + id);
        this.requestedId = id;
    }

    public Long getRequestedId() {
        return requestedId;
    }
}
