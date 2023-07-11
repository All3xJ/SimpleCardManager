package it.unipa.cardmanager.transaction;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TransactionDTO {
    private Long id;

    private Long cardId;

    private Long merchantId;

    private Double amount;

    private Date dateCreated;

    private String merchantName;

    public TransactionDTO(Long id, Long cardId, Long merchantId, Double amount, Date dateCreated, String merchantName){
        this.id=id;
        this.cardId=cardId;
        this.merchantId=merchantId;
        this.amount=amount;
        this.dateCreated=dateCreated;
        this.merchantName=merchantName;
    }
}
