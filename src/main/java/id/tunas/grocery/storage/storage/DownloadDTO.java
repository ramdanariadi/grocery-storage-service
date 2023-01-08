package id.tunas.grocery.storage.storage;

import javax.ws.rs.QueryParam;

public class DownloadDTO {
    @QueryParam("key")
    String key;
    @QueryParam("alias")
    String alias;
    @QueryParam("directory")
    String directory;
    @QueryParam("isThumbnail")
    Boolean isThumbnail;
}
