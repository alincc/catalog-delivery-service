package no.nb.microservices.delivery.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

import java.util.concurrent.Future;

public interface IItemService {

    Future<ItemResource> getItemByIdAsync(String id) throws InterruptedException;
}
