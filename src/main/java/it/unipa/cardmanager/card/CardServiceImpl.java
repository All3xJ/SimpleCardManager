package it.unipa.cardmanager.card;

import it.unipa.cardmanager.user.User;
import it.unipa.cardmanager.user.UserDTO;
import it.unipa.cardmanager.user.UserRepository;
import it.unipa.cardmanager.user.UserService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        Card cardfromdb;
        try {
            cardfromdb = this.cardRepository.findById(cardId).get();   // prelevo carta dal db (sempre se esiste altrimenti lancia eccezione)
        } catch(NoSuchElementException e) {
            throw new NoSuchElementException("Card not found");
        }

        if (cardfromdb.getEnabled()==false) // verifico se è abilitata
            throw new IllegalStateException("Card must be enabled");

        Double newCredit = this.truncate(cardfromdb.getCredit()+amount);    // faccio truncate per evitare bug che diventino numeri con piu di 2 digits oltre le 2 decimali
        cardfromdb.setCredit(newCredit);
        if (cardfromdb.getCredit()<0)   // verifico che credito non sia < 0
            throw new IllegalStateException("Credit cannot be lower than 0");

        this.cardRepository.saveAndFlush(cardfromdb); // fa l'update nel db del nuovo credito

        return newCredit;
    }

    private Double truncate(Double amount){
        DecimalFormat formato = new DecimalFormat("#.##");
        formato.setRoundingMode(RoundingMode.DOWN);
        String troncatoStringa = formato.format(amount);
        double truncated = Double.parseDouble(troncatoStringa);
        return truncated;
    }

//    @Override
//    public boolean checkOwnerExists(Long ownerId) {
//        if (this.userRepository.findById(ownerId).isEmpty())
//            return false;
//        return true;
//    }

    @Override
    public JSONObject createNewCard(Double amount) {

        UserDTO newUser = this.userService.generateAndSaveNewRandomUser();  // innanzitutto genero e salvo un nuovo utente che sarà cardowner della carta che creerò in questo metodo.
        Card newCard = new Card();
        newCard.setOwner(new User());
        newCard.getOwner().setId(newUser.getId());  // setto l'id del cardowner
        newCard.setCredit(amount);
        newCard.setEnabled(true);
        this.cardRepository.saveAndFlush(newCard);  // salvo nel db

        JSONObject jsonObject = new JSONObject();   // creo json
        jsonObject.put("cardId",newCard.getId());
        jsonObject.put("username",newUser.getUsername());
        jsonObject.put("pw",newUser.getPassword());

        return jsonObject;
    }

//    @Override
//    public UserDTO findOwnerById(Long ownerId) {
//        return this.userRepository.findById(ownerId).get().toDTO();
//    }

    @Override
    public boolean blockUnblockCard(Long cardId) {
        Card cardfromdb;
        try {
            cardfromdb = this.cardRepository.findById(cardId).get();   // get() fa throw di NoSuchElementException se card non esiste
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Card does not exist");    // faccio throw con messagio personalizzato
        }

        cardfromdb.setEnabled(!cardfromdb.getEnabled());    // toggle attivo/disattivo
        this.cardRepository.saveAndFlush(cardfromdb);
        return cardfromdb.getEnabled();     // ritorna true se si è attivata, false se si è disattivata.
    }

    @Override
    public CardDTO findCardByOwnerId(Long ownerId) {
        return this.cardRepository.findByOwnerId(ownerId).toDTO();
    }
}
