package com.example.piepayoffers.controller;

import com.example.piepayoffers.dto.OfferResponse;
import com.example.piepayoffers.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping("/offer")
    public ResponseEntity<OfferResponse> saveOffers(@RequestBody Map<String, Object> payload) {
        Map<String, Object> flipkartOfferApiResponse = (Map<String, Object>) payload.get("flipkartOfferApiResponse");
        if (flipkartOfferApiResponse == null) {
            throw new IllegalArgumentException("'flipkartOfferApiResponse' is missing in request body.");
        }
        OfferResponse response = offerService.saveOffers(flipkartOfferApiResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/highest-discount")
    public ResponseEntity<Map<String, Double>> getHighestDiscount(
            @RequestParam double amountToPay,
            @RequestParam String bankName,
            @RequestParam String paymentInstrument) {

        if (amountToPay <= 0) {
            throw new IllegalArgumentException("amountToPay must be > 0");
        }
        if (bankName == null || bankName.isBlank()) {
            throw new IllegalArgumentException("bankName is required");
        }
        if (paymentInstrument == null || paymentInstrument.isBlank()) {
            throw new IllegalArgumentException("paymentInstrument must not be blank.");
        }

        double highestDiscount = offerService.getHighestDiscount(amountToPay, bankName,paymentInstrument);

        return ResponseEntity.ok(Map.of("highestDiscountAmount", highestDiscount));
    }

}
