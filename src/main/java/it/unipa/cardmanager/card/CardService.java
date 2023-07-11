package it.unipa.cardmanager.card;

import org.json.JSONObject;

import java.util.List;

public interface CardService {

    List<CardDTO> getAllCards();

    CardDTO findCardById(Long cardId);

    Double editCredit(Long cardId, Double amount);

    JSONObject createNewCard(Double amount);

    boolean blockUnblockCard(Long cardId);

    CardDTO findCardByOwnerId(Long ownerId);
}
