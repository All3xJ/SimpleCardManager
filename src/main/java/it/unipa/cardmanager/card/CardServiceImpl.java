package it.unipa.cardmanager.card;

import it.unipa.cardmanager.user.User;
import it.unipa.cardmanager.user.UserDTO;
import it.unipa.cardmanager.user.UserRepository;
import it.unipa.cardmanager.user.UserService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;

    private UserRepository userRepository;

    private UserService userService;

    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository, UserService userService){
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<CardDTO> getAllCards() {
        List<CardDTO> cards = new LinkedList<CardDTO>();
        for (Card cr: this.cardRepository.findAll()) {
            cards.add(cr.toDTO());
        }
        return cards;
    }

    @Override
    public CardDTO findCardById(Long cardId){
        return this.cardRepository.findById(cardId).get().toDTO();
    }

    @Override
    public Double editCredit(Long cardId, Double amount){
        Card cardfromdb = this.cardRepository.findById(cardId).get();

        if (cardfromdb.getEnabled()==false)
            throw new IllegalStateException("Credit cannot be lower than 0");

        Double newCredit = cardfromdb.getCredit()+amount;
        cardfromdb.setCredit(newCredit);
        if (cardfromdb.getCredit()<0)
            throw new IllegalStateException("Credit cannot be lower than 0");

        this.cardRepository.saveAndFlush(cardfromdb); // fa l'update nel db del nuovo credito

        return newCredit;
    }

    @Override
    public boolean checkOwnerExists(Long ownerId) {
        if (this.userRepository.findById(ownerId).isEmpty())
            return false;
        return true;
    }

    @Override
    public JSONObject createNewCard(Double amount) {
//        if (this.checkOwnerExists(ownerId)==false)
//            throw new NoSuchElementException("Owner not found: " + ownerId);

        UserDTO newUser = this.userService.generateAndSaveNewRandomUser();
        Card newCard = new Card();
        newCard.setOwner(new User());
        newCard.getOwner().setId(newUser.getId());
        newCard.setCredit(amount);
        newCard.setEnabled(true);
        this.cardRepository.saveAndFlush(newCard);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardId",newCard.getId());
        jsonObject.put("username",newUser.getUsername());
        jsonObject.put("pw",newUser.getPassword());

        return jsonObject;
    }

    @Override
    public UserDTO findOwnerById(Long ownerId) {
        return this.userRepository.findById(ownerId).get().toDTO();
    }

    @Override
    public boolean blockUnblockCard(Long cardId) {  // ritorna true se si è attivata, false se si è disattivata.
        Card cardfromdb;
        try {
            cardfromdb = this.cardRepository.findById(cardId).get();   // get fa throw di NoSuchElementException se card non esiste
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Card does not exist");
        }

        cardfromdb.setEnabled(!cardfromdb.getEnabled());    // toggle attivo/disattivo
        this.cardRepository.saveAndFlush(cardfromdb);
        return cardfromdb.getEnabled();
    }

    @Override
    public CardDTO findCardByOwnerId(Long ownerId) {
//        List<Long> l = this.cardRepository.findCardIdByOwnerId(ownerId);
//
//        return l.get(l.size()-1) ;  // dovrebbe sempre essercene solo una visto che l'owner è sempre e solo uno. ma a scanso di equivoci prendo sempre l'ultimo item.
        return this.cardRepository.findByOwnerId(ownerId).toDTO();
    }
}
