package com.example.piepayoffers.controller;

import com.example.piepayoffers.dto.OfferResponse;
import com.example.piepayoffers.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<OfferResponse> saveOffers(@RequestBody Map<String, Object> payload) {
        Map<String, Object> flipkartResponse = (Map<String, Object>) payload.get("flipkartOfferApiResponse");
        OfferResponse response = offerService.saveOffers(flipkartResponse);
        return ResponseEntity.ok(response);
    }
}
