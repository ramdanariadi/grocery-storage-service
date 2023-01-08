package id.tunas.grocery.storage.exception;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.core.Response;
import java.util.Map;

public class ExceptionHandler {
    @ServerExceptionMapper
    public Response mapException(BadRequestException ex){
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(Map.of("message", ex.getMessage())).build();

//        if(ex instanceof BadRequestException){
//        }
//        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(Map.of("message", "INTERNAL_SERVER_ERROR")).build();
    }
}
