package it.unipa.cardmanager.log;

import it.unipa.cardmanager.card.Card;
import it.unipa.cardmanager.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class LogDTO {
    private Long id;

    private String logType;

    private Long cardId;

    private Long adminId;

    private String info;    // info di newcard e blockunblockcard è amount. info di registeredmerchant e disableenablemerchant è merchant_id

    private Date dateCreated;

    public Long customGetCardId(){
        try{
            return this.getCardId();
        }catch (NullPointerException e){
            return null;
        }
    }
}
