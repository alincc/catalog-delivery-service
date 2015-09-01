package no.nb.microservices.delivery.rest.controller;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.metadata.model.PhotoFile;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;
import no.nb.microservices.delivery.model.textual.TextualResourceRequest;
import no.nb.microservices.delivery.service.IItemService;
import no.nb.microservices.delivery.service.IPhotoService;
import no.nb.microservices.delivery.service.ITextualService;
import no.nb.microservices.delivery.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by andreasb on 14.07.15.
 */
@RestController
public class PhotoController {

    private final IPhotoService photoService;
    private final IItemService itemService;

    @Autowired
    public PhotoController(IPhotoService photoService, IItemService itemService) {
        this.photoService = photoService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/download/photo/{urn}", method = RequestMethod.GET)
    public void downloadPhoto(@PathVariable String urn,
                                        @RequestParam(value = "quality", defaultValue = "4") int quality,
                                        @RequestParam(value = "format", defaultValue = "jpg") String format,
                                        HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {

        PhotoRequest photoRequest = new PhotoRequest() {{
            setUrn(urn);
            setQuality(quality);
            setFormat(format);
        }};

        Future<PhotoFile> photoFileFuture = photoService.getResourceAsync(photoRequest);
        Future<ItemResource> itemResourceFuture = itemService.getItemByIdAsync(photoRequest.getUrn());
        PhotoFile textualResource = photoFileFuture.get();
        ItemResource itemResource = itemResourceFuture.get();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + itemResource.getMetadata().getTitleInfo().getTitle() + ".zip");
        response.getOutputStream().write(textualResource.getContent().getByteArray());

    }
}
