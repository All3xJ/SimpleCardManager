package it.unipa.cardmanager.user;

import it.unipa.cardmanager.card.Card;
import it.unipa.cardmanager.log.LogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final LogService logService;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           LogService logService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    @Override
    public UserDTO getCurrentUser(){   // per ottenere utente attuale, che esso sia admin o merchant o cardowner
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); // trovo l'username del merchant che in questo momento è loggato e ha fatto richiesta
        User user = this.userRepository.findByUsername(username);
        return user.toDTO();
    }

    @Override
    public void saveMerchantUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        // encrypt the password using spring security
        user.setPassword(this.passwordEncoder.encode(userDTO.getPassword()));   // creo hash con bcrypt
        Role role = this.roleRepository.findByName("ROLE_MERCHANT"); // prendo il Role specifico di "ROLE_MERCHANT"
        user.setRoles(Arrays.asList(role));     // quindi salva utente merchant in quanto registrazione la faccio solo di merchant. sarebbe nome del role apposito nella tabella role del db
        this.userRepository.saveAndFlush(user);

        this.logService.addMerchantLog("registeredmerchant",user.getId(),"");
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
        String pw_plaintext = this.randomString();
        newUser.setPassword(this.passwordEncoder.encode(pw_plaintext));
        newUser.setRoles((Arrays.asList(this.roleRepository.findByName("ROLE_CARDOWNER"))));    // questo nuovo utente generato deve ovviamente essere CARDOWNER di ruolo
        newUser.setEnabled(true);
        this.userRepository.saveAndFlush(newUser);
        UserDTO newUserDTO = newUser.toDTO();
        newUserDTO.setPassword(pw_plaintext);   // la salvo in chiaro perche è un utente cardowner generato dall'admin, quindi devo passare le credenziali di questo nuovo cardowner all'admin loggato
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

        this.logService.addMerchantLog("disableenablemerchant",userfromdb.getId(),String.valueOf(userfromdb.isEnabled()));

        return userfromdb.isEnabled();
    }



}