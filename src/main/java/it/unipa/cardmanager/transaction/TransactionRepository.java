package it.unipa.cardmanager.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByMerchant_Id(Long merchantId);

    List<Transaction> findByCardId(Long cardId);

//    List<Transaction> findByCardOwnerId(Long cardOwner);
}
