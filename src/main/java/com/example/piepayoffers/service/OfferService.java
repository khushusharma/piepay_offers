package com.example.piepayoffers.service;

import com.example.piepayoffers.dto.OfferResponse;
import com.example.piepayoffers.models.Offer;
import com.example.piepayoffers.repo.OfferRepository;
import com.example.piepayoffers.utils.OfferParserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;

    public OfferResponse saveOffers(Map<String, Object> flipkartOfferApiResponse) {

        if (flipkartOfferApiResponse == null || flipkartOfferApiResponse.isEmpty()) {
            throw new IllegalArgumentException("Payload is empty or missing.");
        }

        List<Map<String, Object>> offers = (List<Map<String, Object>>) flipkartOfferApiResponse.get("offers");

        if (offers == null) {
            throw new IllegalArgumentException("'offers' key missing in Flipkart payload!");
        }


        int totalIdentified = offers.size();
        int totalNewCreated = 0;

        for (Map<String, Object> offerJson : offers) {
            String offerId = (String) offerJson.get("adjustment_id");

            if (offerId == null || offerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Missing adjustment_id in one of the offers.");
            }

            boolean exists = offerRepository.findByOfferId(offerId).isPresent();
            if (!exists) {
                OfferParserUtils.ParsedOfferDetails parsed = OfferParserUtils.parseSummary((String) offerJson.get("summary"));
                Offer offer = Offer.builder()
                        .offerId(offerId)
                        .adjustmentType((String) offerJson.get("adjustment_type"))
                        .summary((String) offerJson.get("summary"))
                        .banks((List<String>) ((Map<String, Object>) offerJson.get("contributors")).get("banks"))
                        .paymentInstruments((List<String>) ((Map<String, Object>) offerJson.get("contributors")).get("payment_instrument"))
                        .emiMonths((List<String>) ((Map<String, Object>) offerJson.get("contributors")).get("emi_months"))
                        .discountType(parsed.getDiscountType())
                        .discountValue(parsed.getDiscountValue())
                        .percentage(parsed.isPercentage())
                        .minAmount(parsed.getMinAmount())
                        .build();

                offerRepository.save(offer);
                totalNewCreated++;
            }
        }

        return new OfferResponse(totalIdentified, totalNewCreated);
    }


    public double getHighestDiscount(double amountToPay, String bankName) {

        if (amountToPay <= 0) {
            throw new IllegalArgumentException("amountToPay must be greater than zero.");
        }

        if (bankName == null || bankName.isBlank()) {
            throw new IllegalArgumentException("bankName must not be null or empty.");
        }

        List<Offer> offers = offerRepository.findByBankName(bankName);

        if(offers.isEmpty()){
            return 0;
        }

        double highestDiscount = 0;

        for (Offer offer : offers) {
            if (amountToPay < offer.getMinAmount()) {
                continue; // will skip this offer bcz  it's not applicable
            }

            double discount;
            if (offer.isPercentage()) {
                discount = amountToPay * (offer.getDiscountValue() / 100);
            } else {
                discount = offer.getDiscountValue();
            }

            if (discount > highestDiscount) {
                highestDiscount = discount;
            }
        }

        return highestDiscount;
    }

}
