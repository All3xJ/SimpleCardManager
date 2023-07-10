package it.unipa.cardmanager.transaction;

import it.unipa.cardmanager.card.CardDTO;

import java.util.List;

public interface TransactionService {

    void addTransaction(Long cardId, Double amount);

    List<TransactionDTO> getAllTransactions();

//    List<TransactionDTO> filterTransactionsDoneByMerchantId(List<TransactionDTO> transactions);
//
//    List<TransactionDTO> filterTransactionsDoneForCardOwnerId(List<TransactionDTO> transactions);

    List<TransactionDTO> findTransactionsDoneByMerchantId();

    CardDTO findCardByLoggedOwnerId();

    List<TransactionDTO> findTransactionsByCardId(Long cardId);

//    List<TransactionDTO> findTransactionsDoneForCardOwnerId();

}
