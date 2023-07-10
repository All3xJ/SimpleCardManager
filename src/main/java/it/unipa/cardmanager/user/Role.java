package it.unipa.cardmanager.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="role")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;

    @Column(nullable=false, unique=true)
    private String name;

    @ManyToMany(mappedBy="roles") // roles di qua si riferisce all'attributo roles nella classe entity User
    private List<User> users;
}