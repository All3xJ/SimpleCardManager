package it.unipa.cardmanager.card;

import it.unipa.cardmanager.user.UserDTO;
import org.json.JSONObject;

import java.util.List;

public interface CardService {

    List<CardDTO> getAllCards();

    CardDTO findCardById(Long cardId);

    Double editCredit(Long cardId, Double amount);

    JSONObject createNewCard(Double amount);

//    UserDTO findOwnerById(Long ownerId);
//
//    boolean checkOwnerExists(Long ownerId);

    boolean blockUnblockCard(Long cardId);

    CardDTO findCardByOwnerId(Long ownerId);
}
