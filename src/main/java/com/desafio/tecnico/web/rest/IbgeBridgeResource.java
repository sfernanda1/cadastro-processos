package com.desafio.tecnico.web.rest;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/ibge-bridge")
public class IbgeBridgeResource {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/estados")
    public ResponseEntity<List<Map<String, Object>>> getEstados() {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome";
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }


    @GetMapping("/estados/{ufId}/municipios")
    public ResponseEntity<List<Map<String, Object>>> getMunicipios(@PathVariable Integer ufId) {
        String url = String.format("https://servicodados.ibge.gov.br/api/v1/localidades/estados/%d/municipios?orderBy=nome", ufId);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }
}
