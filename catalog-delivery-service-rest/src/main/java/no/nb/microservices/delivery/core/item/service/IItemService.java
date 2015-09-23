package no.nb.microservices.delivery.core.item.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

import java.util.concurrent.Future;

public interface IItemService {

    Future<ItemResource> getItemByIdAsync(String id);
    Future<ItemResource> getItemByUrnAsync(String urn);
}
