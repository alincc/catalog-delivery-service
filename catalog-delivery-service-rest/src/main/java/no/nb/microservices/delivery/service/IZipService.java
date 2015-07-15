package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.generic.ItemResource;

import java.io.File;
import java.util.List;

/**
 * Created by andreasb on 14.07.15.
 */
public interface IZipService {
    File zipIt(String outputZipPath, List<ItemResource> itemResources);
}
