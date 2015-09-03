package no.nb.microservices.delivery.service.cloud;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.delivery.repository.ItemRepository;
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

    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    @HystrixCommand(fallbackMethod = "getDefaultItem")
    public Future<ItemResource> getItemByIdAsync(String id) {
        LOGGER.info("Fetching item from catalog-item-service by id " + id);
        return new AsyncResult<ItemResource>() {
            @Override
            public ItemResource invoke() {
                return itemRepository.getById(id);
            }
        };
    }
    
    private ItemResource getDefaultItem(String id) {
        LOGGER.warn("Failed to get item from catalog-item-service. Returning default item with id " + id);
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle("No title");
        Metadata metadata = new Metadata();
        metadata.setTitleInfo(titleInfo);
        ItemResource item = new ItemResource();
        item.setMetadata(metadata);
        return item;
    }
}
