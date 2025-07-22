package com.example.piepayoffers.repo;

import com.example.piepayoffers.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<Offer> findByOfferId(String offerId);

    @Query("SELECT o FROM Offer o JOIN o.banks b WHERE b = :bankName")
    List<Offer> findByBankName(@Param("bankName") String bankName);

}
