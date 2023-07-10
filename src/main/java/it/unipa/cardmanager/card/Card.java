package it.unipa.cardmanager.card;

import it.unipa.cardmanager.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@Entity
@Table(name="card")
@Getter
@Setter
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="owner_id", unique = true, referencedColumnName = "id")
    private User owner;

    //@Column(nullable = false)
//    @Digits(integer = 8 /*precision*/, fraction = 2 /*scale*/)
    private Double credit;

    @Column(nullable = false)
    private Boolean enabled;

    public void setCredit(Double credit){
        this.credit=this.truncate(credit);
    }

    private Double truncate(Double credit){
        DecimalFormat formato = new DecimalFormat("#.##");
        formato.setRoundingMode(RoundingMode.DOWN);
        String troncatoStringa = formato.format(credit);
        Double troncato = Double.parseDouble(troncatoStringa);
        return troncato;
    }

    public CardDTO toDTO(){
        return new CardDTO(this.id,this.owner.getId(),this.credit,this.enabled);
    }
}
