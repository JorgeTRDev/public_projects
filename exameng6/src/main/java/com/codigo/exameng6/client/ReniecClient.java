package com.codigo.exameng6.client;

import com.codigo.exameng6.aggregates.responses.ReniecResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ReniecClient interface: Interface que permite el consumo a apis externas
 *
 * @version 1.0
 */
@FeignClient(name = "reniec-client", url = "https://api.apis.net.pe/v2/reniec/")
public interface ReniecClient {

    @GetMapping("/dni")
    ReniecResponse getPersonaReniec(@RequestParam("numero") String numero,
                                    @RequestHeader("Authorization") String authorization);
}