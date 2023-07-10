package it.unipa.cardmanager.user;

import it.unipa.cardmanager.card.Card;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        // encrypt the password using spring security
        user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));
        Role role = this.roleRepository.findByName("ROLE_MERCHANT"); // quindi per ora salva solo merchant. sarebbe nome del role apposito nella tabella role del db
        user.setRoles(Arrays.asList(role));
        this.userRepository.saveAndFlush(user);
    }



    @Override
    public UserDTO findByUsername(String username) {
        try {
            return userRepository.findByUsername(username).toDTO();
        }catch(NullPointerException e) {
            return null;
        }
    }

//    private UserDTO mapToUserDto(User user){
//        UserDTO userDto = new UserDTO();
//        userDto.setUsername(user.getUsername());
//        return userDto;
//    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream()
                .map((user) -> user.toDTO())
                .collect(Collectors.toList());
    }

    private String randomString(){
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random = new Random();
        int minLength = 6;
        int maxLength = 10;

        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    @Override
    public UserDTO generateAndSaveNewRandomUser() {
        User newUser = new User();
        newUser.setUsername(this.randomString());
        String pw_plaintext = this.randomString();    // la salvo perche siccome è un utente cardowner generato e non mi serve segretezza, allora voglio passare la pw in chiaro quindi la salvo, altrimenti mi metterebbe quella encoded
        newUser.setPassword(this.passwordEncoder.encode(pw_plaintext));
        newUser.setRoles((Arrays.asList(this.roleRepository.findByName("ROLE_CARDOWNER"))));
        newUser.setEnabled(true);
        this.userRepository.saveAndFlush(newUser);
        UserDTO newUserDTO = newUser.toDTO();
        newUserDTO.setPassword(pw_plaintext);
        return newUserDTO;
    }

    @Override
    public boolean disablenablemerchant(String merchantusername) {
        User userfromdb;
        try {
            userfromdb = this.userRepository.findByUsername(merchantusername);   // get fa throw di NoSuchElementException se card non esiste
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("Card does not exist");
        }
        if (!Objects.equals(userfromdb.getRoles().get(0).getName(), "ROLE_MERCHANT"))   // CONTROLLO SE È UN MERCHANT, ALTRIMENTI NON LO DISABILITO
            throw new IllegalStateException("You can only enable/disable merchant users!");

        userfromdb.setEnabled(!userfromdb.isEnabled());    // toggle attivo/disattivo
        this.userRepository.save(userfromdb);
        return userfromdb.isEnabled();
    }



}