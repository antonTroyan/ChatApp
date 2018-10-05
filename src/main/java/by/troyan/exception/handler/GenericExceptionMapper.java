package by.troyan.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Generic exception mapper. Converts all possible exceptions in the Response
 * object and send it to a client. It is possible to flexibly manage what
 * message will be return depending on the exception type.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ExceptionProperties("Some error code, 500 or something",
                            exception.getMessage()))
                    .build();
    }
}
