package it.unipa.cardmanager.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findByOwnerId(Long ownerId);
}
