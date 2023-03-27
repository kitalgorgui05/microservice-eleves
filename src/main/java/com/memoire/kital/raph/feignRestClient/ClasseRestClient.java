package com.memoire.kital.raph.feignRestClient;

import com.memoire.kital.raph.restClient.ClasseClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "${jhipster.clientApp.name}", url = "http://localhost:8802")
public interface ClasseRestClient {
    @GetMapping("/api/classes/{id}")
    ResponseEntity<ClasseClient> getClasse(@PathVariable(name = "id") String id);
}
