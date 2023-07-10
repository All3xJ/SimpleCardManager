package it.unipa.cardmanager.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/registermerchant")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDTO());
        return "admin/registermerchant";
    }

    // gestisce la richiesta post all'url save, dopo che utente fa submit
    @PostMapping("/admin/registermerchant/save")
    public String registration(@Valid @ModelAttribute("user") UserDTO userDTO,
                               BindingResult result,
                               Model model){

        UserDTO existingUser = userService.findByUsername(userDTO.getUsername());

        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null,
                    "There is already an account registered with the same username");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDTO);
            return "admin/registermerchant";
        }

        userService.saveUser(userDTO);
        return "redirect:/admin/registermerchant?success";
    }

    @GetMapping("/admin/users")
    public String users(Model model){
        model.addAttribute("users", this.userService.findAllUsers());
        return "admin/users";
    }


    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/disableenablemerchant")
    public String blockUnblockMerchant(){
        return "admin/disableenablemerchant";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/admin/disableenablemerchant")
    public ResponseEntity<Boolean> blockUnblockMerchant(@RequestParam(name = "merchantusername") String merchantusername){
        return ResponseEntity.ok(this.userService.disablenablemerchant(merchantusername));  // se qualcosa va storto ci sarà throw di eccezione apposita.
    }
}