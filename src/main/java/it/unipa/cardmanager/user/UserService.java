package it.unipa.cardmanager.user;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);

    UserDTO findByUsername(String username);

    List<UserDTO> findAllUsers();

    UserDTO generateAndSaveNewRandomUser();

    boolean disablenablemerchant(String merchantusername);
}