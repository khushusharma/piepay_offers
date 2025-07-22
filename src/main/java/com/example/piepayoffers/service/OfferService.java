package com.example.piepayoffers.service;

import com.example.piepayoffers.dto.OfferResponse;
import com.example.piepayoffers.models.Offer;
import com.example.piepayoffers.repo.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferResponse saveOffers(Map<String, Object> flipkartOfferApiResponse) {
        // Extract the 'offers' array from the JSON
        List<Map<String, Object>> offers = (List<Map<String, Object>>) flipkartOfferApiResponse.get("offers");

        int totalIdentified = offers.size();
        int totalNewCreated = 0;

        for (Map<String, Object> offerJson : offers) {
            String offerId = (String) offerJson.get("adjustment_id");

            boolean exists = offerRepository.findByOfferId(offerId).isPresent();
            if (!exists) {
                Offer offer = Offer.builder()
                        .offerId(offerId)
                        .adjustmentType((String) offerJson.get("adjustment_type"))
                        .summary((String) offerJson.get("summary"))
                        .banks((List<String>) ((Map<String, Object>) offerJson.get("contributors")).get("banks"))
                        .paymentInstruments((List<String>) ((Map<String, Object>) offerJson.get("contributors")).get("payment_instrument"))
                        .emiMonths((List<String>) ((Map<String, Object>) offerJson.get("contributors")).get("emi_months"))
                        // You may parse discountValue, percentage, minAmount if available
                        .build();

                offerRepository.save(offer);
                totalNewCreated++;
            }
        }

        return new OfferResponse(totalIdentified, totalNewCreated);
    }
}
