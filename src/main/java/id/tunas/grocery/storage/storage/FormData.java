package id.tunas.grocery.storage.storage;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

import javax.ws.rs.core.MediaType;
import java.io.File;

public class FormData {
    @RestForm("file")
    public File file;
    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String filename;
    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public String directory;
    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    public Boolean isPublic;

}
