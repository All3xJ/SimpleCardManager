package it.unipa.cardmanager.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO
{
    private Long id;

    private String username;

    private String password;

    private boolean enabled;

    private String role;
}