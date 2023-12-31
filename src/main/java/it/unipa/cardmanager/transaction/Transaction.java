package it.unipa.cardmanager.transaction;

import it.unipa.cardmanager.card.Card;
import it.unipa.cardmanager.card.CardDTO;
import it.unipa.cardmanager.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // per fare auto increement e non dover specificare manualmente id quando si fa insert
    private Long id;

    @ManyToOne  // perchè possono esserci piu transazioni per una stessa carta
    @JoinColumn(name = "card_id", nullable = false, referencedColumnName = "id")
    private Card card;

    @ManyToOne  // perchè possono esserci piu transazioni eseguite da uno stesso merchant
    @JoinColumn(name = "merchant_id", nullable = false, referencedColumnName = "id")
    private User merchant;

    @Column(nullable = false)
//    @Digits(integer = 8 /*precision*/, fraction = 2 /*scale*/)
    private Double amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateCreated;

    public TransactionDTO toDTO() {
        return new TransactionDTO(this.id, this.card.getId(), this.merchant.getId(), this.amount, this.dateCreated, this.merchant.getUsername());
    }

}