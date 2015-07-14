package no.nb.microservices.delivery.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.microservice.CatalogItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by andreasb on 15.06.15.
 */
@Service
public class ItemService implements IItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    private CatalogItemService catalogItemService;

    @Autowired
    public ItemService(CatalogItemService catalogItemService) {
        this.catalogItemService = catalogItemService;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultItem")
    public Future<ItemResource> getItemByIdAsync(String id) throws InterruptedException {
        LOGGER.info("Fetching item from catalog-item-service by id " + id);
        return new AsyncResult<ItemResource>() {
            @Override
            public ItemResource invoke() {
                return catalogItemService.getById(id);
            }
        };
    }
    
    private ItemResource getDefaultItem(String id) {
        LOGGER.warn("Failed to get item from catalog-item-service. Returning default item with id " + id);
        ItemResource item = new ItemResource();
        return item;
    }
}
