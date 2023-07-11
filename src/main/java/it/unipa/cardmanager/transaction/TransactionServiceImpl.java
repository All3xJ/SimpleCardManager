package it.unipa.cardmanager.transaction;

import it.unipa.cardmanager.card.Card;
import it.unipa.cardmanager.card.CardDTO;
import it.unipa.cardmanager.card.CardService;
import it.unipa.cardmanager.user.User;
import it.unipa.cardmanager.user.UserDTO;
import it.unipa.cardmanager.user.UserRepository;
import it.unipa.cardmanager.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    private final CardService cardService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService, CardService cardService){
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.cardService = cardService;
    }

    @Override
    public void addTransaction(Long cardId, Double amount) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        String humanDate = formatter.format(date);  // trasformo in stringa
        //System.out.println(humanDate);
        Transaction newtransaction = new Transaction();
        newtransaction.setCard(new Card()); // creo nuovo oggetto card
        newtransaction.getCard().setId(cardId); // e setto id della carta in questione
        newtransaction.setMerchant(new User());
        newtransaction.getMerchant().setId(this.userService.getCurrentUser().getId());  // stesso discorso per merchantid che sta aggiungendo questa transazione
        newtransaction.setAmount(amount);
        newtransaction.setDateCreated(date);
        this.transactionRepository.saveAndFlush(newtransaction);
    }

    private List<TransactionDTO> convertTransactionListToDTO(List<Transaction> transactions){   // trasforma lista di Transaction a lista di DTO
        List<TransactionDTO> transactionDTOList = new LinkedList<>();
        for (Transaction transaction: transactions)
            transactionDTOList.add(transaction.toDTO());

        return transactionDTOList;
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {

        List<TransactionDTO> sortList = this.convertTransactionListToDTO(this.transactionRepository.findAll());
        sortList.sort((tran1, tran2) -> tran2.getDateCreated().compareTo(tran1.getDateCreated()));  // faccio il sorting delle transazioni in ordine decrescente di timestamp in modo da visualizzare nell'html prima quelle piu recenti

        return sortList;
    }

    @Override
    public List<TransactionDTO> findTransactionsDoneByMerchantId() {
        Long merchantId = this.userService.getCurrentUser().getId();    // trovo id merchant attualmente loggato che ha fatto richiesta

        List<Transaction> transactions = this.transactionRepository.findByMerchant_Id(merchantId);  // trovo transazioni fatte da questo merchant

        List<TransactionDTO> sortList = this.convertTransactionListToDTO(transactions);
        sortList.sort((tran1, tran2) -> tran2.getDateCreated().compareTo(tran1.getDateCreated()));

        return sortList;
    }

    @Override
    public CardDTO findCardByLoggedOwnerId(){
        Long cardOwnerId = this.userService.getCurrentUser().getId();   // trovo attuale cardowner loggato che ha fatto richiesta
        return this.cardService.findCardByOwnerId(cardOwnerId);
    }

    @Override
    public List<TransactionDTO> findTransactionsByCardId(Long cardId) {
        List<Transaction> transactions = this.transactionRepository.findByCardId(cardId);

        List<TransactionDTO> sortList = this.convertTransactionListToDTO(transactions);
        sortList.sort((tran1, tran2) -> tran2.getDateCreated().compareTo(tran1.getDateCreated()));

        return sortList;
    }


}
