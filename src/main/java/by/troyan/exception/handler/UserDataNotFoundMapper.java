package by.troyan.exception.handler;

import by.troyan.exception.UserDataNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for UserDataNotFoundException.
 */
@Provider
public class UserDataNotFoundMapper implements ExceptionMapper<UserDataNotFoundException> {

    @Override
    public Response toResponse(UserDataNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ExceptionProperties("404", e.getMessage()))
                    .build();
    }
}
