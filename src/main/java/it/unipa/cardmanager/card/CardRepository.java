package it.unipa.cardmanager.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
//    @Query("SELECT c.id FROM Card c WHERE c.ownerId = :ownerId")    // si lo so sembra sbagliata ma è cosi, camelcase in java corrispondono a _ in db
//    List<Long> findCardIdByOwnerId(@Param("ownerId") Long ownerId);
    Card findByOwnerId(Long ownerId);
}
