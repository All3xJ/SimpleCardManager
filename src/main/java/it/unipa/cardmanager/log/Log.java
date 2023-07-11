package it.unipa.cardmanager.log;

import it.unipa.cardmanager.card.Card;
import it.unipa.cardmanager.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="log")
@Getter
@Setter
@NoArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // per fare auto increement e non dover specificare manualmente id quando si fa insert
    private Long id;

    @Column(nullable = false)
    private String logType;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = true, unique = false, referencedColumnName = "id")  // può essere null, nel caso in cui sia una operazione di creazione/dsabilito di un merchant, in cui quindi carta non c'entra e non ci sarà. inoltre obv non deve essere unique
    private Card card;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = true, unique = false, referencedColumnName = "id")  // può essere null, nel caso in cui sia una operazione di creazione/dsabilito di un merchant, in cui quindi carta non c'entra e non ci sarà. inoltre obv non deve essere unique
    private User merchant;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false, unique = false, referencedColumnName = "id")   // obv non deve essere unique
    private User admin;

    @Column(nullable = false)
    private String info;    // info di newcard e blockunblockcard è amount. info di registeredmerchant e disableenablemerchant è merchant_id

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateCreated;   // questo camelCase IMPONE che nel db sia owner_id LOL

    public LogDTO toDTO(){
        try {
            return new LogDTO(this.id, this.logType, null, this.merchant.getId(), this.admin.getId(), this.info, this.dateCreated, this.admin.getUsername(), this.merchant.getUsername());
        } catch (NullPointerException e){
            return new LogDTO(this.id, this.logType, this.card.getId(), null, this.admin.getId(), this.info, this.dateCreated, this.admin.getUsername(), null);
        }
    }
}