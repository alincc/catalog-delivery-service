package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.delivery.metadata.model.PhotoFile;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.repository.TextualGeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 31.08.15.
 */
@Service
public class PhotoService implements IPhotoService {

    private final TextualGeneratorRepository textualGeneratorRepository;

    @Autowired
    public PhotoService(TextualGeneratorRepository textualGeneratorRepository) {
        this.textualGeneratorRepository = textualGeneratorRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultResource",
            commandProperties = {
                    @HystrixProperty(name="execution.timeout.enabled", value="600000")
            })
    public Future<PhotoFile> getResourceAsync(PhotoRequest photoRequest) {
        return new AsyncResult<PhotoFile>() {
            @Override
            public PhotoFile invoke() {
                return getResource(photoRequest);
            }
        };
    }

    @Override
    public PhotoFile getResource(PhotoRequest photoRequest) {
        ByteArrayResource response = textualGeneratorRepository.generate(Arrays.asList(photoRequest.getUrn()), null, null, false, Arrays.asList(photoRequest.getQuality() + ""), photoRequest.getFilename(), photoRequest.getFormat());
        PhotoFile photoFile = new PhotoFile();
        photoFile.setContent(response);
        photoFile.setFilename(photoRequest.getFilename() + ".zip");
        photoFile.setFileSizeInBytes(response.contentLength());
        photoFile.setFormat(photoRequest.getFormat());
        photoFile.setUrn(photoRequest.getUrn());
        photoFile.setQuality(photoRequest.getQuality());
        return photoFile;
    }

    private PhotoFile getDefaultResource(PhotoRequest photoRequest) {
        PhotoFile photoFile = new PhotoFile();
        return photoFile;
    }
}
