package com.sunrise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class Controller {

    @Autowired
    CallExternalApi callExternalApi;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/sunrises")
   Result all() throws Exception {
        log.info("Getting info for sunrise time !");
        return  callExternalApi.getSunriseInfo(restTemplate);

    }
}

