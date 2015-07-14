package no.nb.microservices.delivery.service;

import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialRequest;
import no.nb.microservices.delivery.model.printedMaterial.PrintedMaterialResource;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 09.07.15.
 */
public interface IPrintedMaterialService {
    Future<PrintedMaterialResource> getPrintedMaterialResourceAsync(PrintedMaterialRequest printedMaterialRequest);
}
