package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.metadata.model.PhotoFile;
import no.nb.microservices.delivery.metadata.model.TextualFile;
import no.nb.microservices.delivery.model.photo.PhotoRequest;
import no.nb.microservices.delivery.model.textual.TextualFileRequest;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 28.08.15.
 */
public interface IPhotoService {
        Future<PhotoFile> getResourceAsync(PhotoRequest photoRequest);
        PhotoFile getResource(PhotoRequest photoRequest);
}
