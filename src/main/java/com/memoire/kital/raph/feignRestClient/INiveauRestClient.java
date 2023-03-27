package com.memoire.kital.raph.feignRestClient;

import com.memoire.kital.raph.restClient.NiveauClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${jhipster.clientApp.name}",url = "http://localhost:8082/")
public interface INiveauRestClient {
    @GetMapping("/niveaus/{id}")
    ResponseEntity<NiveauClient> getNiveau(@PathVariable String id);
}
