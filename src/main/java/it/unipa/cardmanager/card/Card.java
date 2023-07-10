package it.unipa.cardmanager.card;

import it.unipa.cardmanager.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
//    @Digits(integer = 8 /*precision*/, fraction = 2 /*scale*/)
    private Double credit;

    @Column(nullable = false)
    private Boolean enabled;

    public CardDTO toDTO(){
        return new CardDTO(this.id,this.owner.getId(),this.credit,this.enabled);
    }
}
