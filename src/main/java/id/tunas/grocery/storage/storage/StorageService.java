package id.tunas.grocery.storage.storage;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import id.tunas.grocery.storage.exception.BadRequestException;
import io.quarkus.runtime.StartupEvent;
import org.apache.tika.Tika;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@ApplicationScoped
public class StorageService {

    AmazonS3 s3Client;
    @ConfigProperty(name = "aws.s3.bucket.name")
    Optional<String> bucketName;

    final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    public void onStart(@Observes StartupEvent ev){
        s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
    }

    public void upload(FormData data) throws BadRequestException {
        if(Strings.isNullOrEmpty(data.filename)){
            throw new BadRequestException("BAD_REQUEST");
        }

        if(!Strings.isNullOrEmpty(data.directory)){
            data.filename = data.directory +"/"+ data.filename;
        }

        PutObjectRequest upload = new PutObjectRequest(bucketName.orElse(""), data.filename, new File(data.file.getAbsolutePath()));
        PutObjectResult putObjectResult = s3Client.putObject(upload);
        LOGGER.info("upload file status {}", putObjectResult.getVersionId());
    }

    public byte[] download(DownloadDTO download) throws IOException, BadRequestException {
        if(Strings.isNullOrEmpty(download.key)){
            throw new BadRequestException("INVALID_KEY");
        }

        String file = download.key;
        if(!Strings.isNullOrEmpty(download.directory)){
            file = download.directory + File.separator + file;
        }

        S3Object s3Object = s3Client.getObject(bucketName.orElse(""), file);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] buffer = s3ObjectInputStream.readAllBytes();
        s3ObjectInputStream.close();
        return buffer;
    }
}
