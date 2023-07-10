package it.unipa.cardmanager.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CardDTO {
    private Long id;
    private Long ownerId;
    private Double credit;
    private Boolean enabled;
}
