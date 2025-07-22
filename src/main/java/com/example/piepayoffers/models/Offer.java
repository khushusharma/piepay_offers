package com.example.piepayoffers.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String offerId; // adjustment_id

    private String adjustmentType; // INSTANT_DISCOUNT, CASHBACK_ON_CARD

    @Column(columnDefinition = "TEXT")
    private String summary; // offer description

    @ElementCollection
    @CollectionTable(name = "offer_banks", joinColumns = @JoinColumn(name = "offer_id"))
    @Column(name = "bank")
    private List<String> banks;

    @ElementCollection
    @CollectionTable(name = "offer_payment_instruments", joinColumns = @JoinColumn(name = "offer_id"))
    @Column(name = "payment_instrument")
    private List<String> paymentInstruments;

    @ElementCollection
    @CollectionTable(name = "offer_emi_months", joinColumns = @JoinColumn(name = "offer_id"))
    @Column(name = "emi_month")
    private List<String> emiMonths;

    private String discountType; // optional: flat, percentage, cashback type

    private double discountValue; // amount or % value

    private boolean percentage; // true if discountValue is %

    private double minAmount; // minimum transaction value
}

