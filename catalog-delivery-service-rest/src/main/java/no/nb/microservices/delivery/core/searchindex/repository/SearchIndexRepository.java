package no.nb.microservices.delivery.core.searchindex.repository;

import no.nb.microservices.catalogsearchindex.SearchResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("catalog-search-index-service")
public interface SearchIndexRepository {

    @RequestMapping(value = "/catalog/v1/search", method = RequestMethod.GET)
    SearchResource search(@RequestParam(value = "q") String queryString,
                          @RequestParam(value = "aggs", required = false) String[] aggs,
                          @RequestParam("page") int pageNumber,
                          @RequestParam("size") int pageSize, @RequestParam("sort") List<String> sort);
}
