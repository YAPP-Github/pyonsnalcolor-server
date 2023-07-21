package com.pyonsnalcolor.batch.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "gs25", url = "http://gs25.gsretail.com")
public interface GS25Client {
    @RequestMapping(method = RequestMethod.POST, value = "/gscvs/ko/products/event-goods-search", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Object getEventProducts(@RequestHeader("Cookie") String cookie,
                            @RequestParam("CSRFToken") String csrfToken,
                            @RequestBody GS25EventRequestBody requestBody);

    @RequestMapping(method = RequestMethod.POST, value = "/products/youus-freshfoodDetail-search", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Object getPbProducts(@RequestHeader("Cookie") String cookie,
                         @RequestParam("CSRFToken") String csrfToken,
                         @RequestBody GS25PbRequestBody requestBody);
}
