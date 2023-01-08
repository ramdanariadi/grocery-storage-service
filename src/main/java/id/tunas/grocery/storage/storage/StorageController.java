package id.tunas.grocery.storage.storage;

import id.tunas.grocery.storage.exception.BadRequestException;
import org.apache.http.Header;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/storage")
public class StorageController {
    @Inject
    StorageService storageService;

    private final Logger LOGGER = LoggerFactory.getLogger(StorageController.class);

    @POST
    @Path("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(FormData data) throws BadRequestException {
        storageService.upload(data);
        return Response.ok().entity(Map.of()).build();
    }

    @GET
    @Path("/")
    public Response download(@BeanParam DownloadDTO download) throws IOException, BadRequestException {
        byte[] bytes = storageService.download(download);
        Response.ResponseBuilder response = Response.ok(bytes);
        String mimeType = new Tika().detect(bytes);
        response.header("Content-Type", mimeType);
        if(!mimeType.startsWith("image")){
            response.header("Content-Disposition","attachment;filename="+download.key);
        }
        return response.build();
    }
}
