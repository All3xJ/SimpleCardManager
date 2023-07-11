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

    private Long merchantId;

    private Long adminId;

    private String info;    // info di newcard e blockunblockcard è amount. info di registeredmerchant e disableenablemerchant è merchant_id

    private Date dateCreated;

    private String adminUsername;

    private String merchantUsername;

    public String getCustomInfoForTable(){
        String logtype = this.logType;

        if(logtype.equals("newcard"))
            return "cardId: "+this.cardId+"<br>amount: "+this.info;
        else if (logtype.equals("blockunblockcard"))
            return "cardId: "+this.cardId+"<br>state: "+(this.info.equals("true") ? "enabled" : "disabled");
        else if (logtype.equals("registeredmerchant"))
            return "merchantusername: "+this.merchantUsername;
        else if (logtype.equals("disableenablemerchant"))
            return "merchantusername: "+this.merchantUsername+"<br>state: "+(this.info.equals("true") ? "enabled" : "disabled");

        return "";
    }
}
