package it.unipa.cardmanager.transaction;

import it.unipa.cardmanager.card.Card;
import it.unipa.cardmanager.card.CardDTO;
import it.unipa.cardmanager.card.CardService;
import it.unipa.cardmanager.user.User;
import it.unipa.cardmanager.user.UserDTO;
import it.unipa.cardmanager.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService{

    private TransactionRepository transactionRepository;

    private UserRepository userRepository;

    private CardService cardService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, CardService cardService){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.cardService = cardService;
    }

    private UserDTO getCurrentUser(){   // per ottenere utente attuale, che esso sia merchant o cardowner ecc
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); // trovo l'username  del merchant che in questo momento è loggato e ha fatto richiesta
        User user = this.userRepository.findByUsername(username);
        return user.toDTO();
    }

    @Override
    public void addTransaction(Long cardId, Double amount) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        String humanDate = formatter.format(date);
        System.out.println(humanDate);
        Transaction newtransaction = new Transaction();
        newtransaction.setCard(new Card());
        newtransaction.getCard().setId(cardId);
        newtransaction.setMerchant(new User());
        newtransaction.getMerchant().setId(this.getCurrentUser().getId());
        newtransaction.setAmount(amount);
        newtransaction.setDateCreated(date);
        this.transactionRepository.saveAndFlush(newtransaction);
    }

    private List<TransactionDTO> convertTransactionListToDto(List<Transaction> transactions){
        List<TransactionDTO> transactionDTOList = new LinkedList<>();
        for (Transaction transaction: transactions)
            transactionDTOList.add(transaction.toDTO());

        return transactionDTOList;
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {
//        List<TransactionDTO> transactions = new LinkedList<TransactionDTO>();
//        for (Transaction transaction : this.transactionRepository.findAll()){
//            String merchantUsername = this.userRepository.findById(transaction.getMerchant().getUsername();
//            transactions.add(transaction.toDTO(merchantUsername));   // versione del toDTO che salva anche merchantUsername oltre agli altri attributi. cosi posso visualizzarlo nell'html
//        }
//        return transactions;

        List<TransactionDTO> sortList = this.convertTransactionListToDto(this.transactionRepository.findAll());
        sortList.sort((tran1, tran2) -> tran2.getDateCreated().compareTo(tran1.getDateCreated()));

        return sortList;
    }

    @Override
    public List<TransactionDTO> findTransactionsDoneByMerchantId() {
        Long merchantId = this.getCurrentUser().getId();

        List<Transaction> transactions = this.transactionRepository.findByMerchant_Id(merchantId);

        List<TransactionDTO> sortList = this.convertTransactionListToDto(transactions);
        sortList.sort((tran1, tran2) -> tran2.getDateCreated().compareTo(tran1.getDateCreated()));

        return sortList;
    }

    @Override
    public CardDTO findCardByLoggedOwnerId(){
        Long cardOwnerId = this.getCurrentUser().getId();   // trovo attuale cardowner loggato
        return this.cardService.findCardByOwnerId(cardOwnerId);
    }

    public List<TransactionDTO> findTransactionsByCardId(Long cardId) {
        List<Transaction> transactions = this.transactionRepository.findByCardId(cardId);

        List<TransactionDTO> sortList = this.convertTransactionListToDto(transactions);
        sortList.sort((tran1, tran2) -> tran2.getDateCreated().compareTo(tran1.getDateCreated()));

        return sortList;
    }

//    @Override
//    public List<TransactionDTO> findTransactionsDoneForCardOwnerId() {
//        Long cardOwnerId = this.getCurrentUser().getId();   // trovo attuale cardowner loggato
//
//        List<Transaction> transactions = this.transactionRepository.findByCardOwnerId(cardOwnerId);
//
//        return this.convertTransactionListToDto(transactions);
//    }











//    @Override
//    public List<TransactionDTO> filterTransactionsDoneByMerchantId(List<TransactionDTO> transactions) {
//        List<TransactionDTO> transactionsDoneBySpecificMerchant = new LinkedList<TransactionDTO>();
//        for (TransactionDTO transaction : transactions){
//            if (Objects.equals(transaction.getMerchantId(), this.getCurrentUser().getId()))
//                transactionsDoneBySpecificMerchant.add(transaction);
//        }
//        return transactionsDoneBySpecificMerchant;
//    }
//
//    @Override
//    public List<TransactionDTO> filterTransactionsDoneForCardOwnerId(List<TransactionDTO> transactions) {
//        List<TransactionDTO> transactionsDoneForCardOwnerId = new LinkedList<TransactionDTO>();
//
//        Long idcardOwner = this.cardService.searchCardByOwnerId(this.getCurrentUser().getId()).getId(); // carta che possiede l'attuale card owner loggato
//
//        for (TransactionDTO transaction : transactions){
//            if (Objects.equals(transaction.getCardId(), idcardOwner)) {
//                String merchantUsername = this.userRepository.findById(transaction.getMerchantId()).get().getUsername();
//                transaction.setMerchantName(merchantUsername);  // cosi che ho anche username del merchant nel DTO Transaction in modo da poterlo visualizzare nell'html
//                transactionsDoneForCardOwnerId.add(transaction);
//            }
//        }
//        return transactionsDoneForCardOwnerId;
//    }
}
