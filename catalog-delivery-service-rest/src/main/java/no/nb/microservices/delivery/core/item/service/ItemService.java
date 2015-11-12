package no.nb.microservices.delivery.core.item.service;

import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.delivery.core.item.repository.ItemRepository;
import no.nb.microservices.delivery.core.searchindex.service.ISearchIndexService;
import no.nb.microservices.delivery.core.searchindex.service.SearchIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class ItemService implements IItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemService.class);

    private ItemRepository itemRepository;
    private ISearchIndexService searchIndexService;

    @Autowired
    public ItemService(ItemRepository itemRepository, SearchIndexService searchIndexService) {
        this.itemRepository = itemRepository;
        this.searchIndexService = searchIndexService;
    }

    @Async
    @Override
    public Future<ItemResource> getItemByIdAsync(String id) {
        LOGGER.info("Fetching item from catalog-item-service by id " + id);
        return new AsyncResult<>(itemRepository.getById(id));
    }

    @Async
    @Override
    public Future<ItemResource> getItemByUrnAsync(String urn) {
        String id = searchIndexService.getId(urn);
        return new AsyncResult<>(itemRepository.getById(id));
    }
}
