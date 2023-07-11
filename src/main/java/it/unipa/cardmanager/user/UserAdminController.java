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

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/registermerchant")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDTO());
        return "admin/registermerchant";
    }

    // gestisce la richiesta post all'url save, dopo che utente fa submit
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/admin/registermerchant/save")
    public String registration(@Valid @ModelAttribute("user") UserDTO userDTO,
                               BindingResult result,    // BindingResult è interfaccia che estende Errors
                               Model model){

        UserDTO existingUser = userService.findByUsername(userDTO.getUsername());

        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) // se c'è un problema nella registrazione
            return "redirect:/admin/registermerchant?error";    // fa redirect mettendo ?error e poi verrà visualizzato errore dal motore thymeleaf della pagina registermerchant.html

        userService.saveMerchantUser(userDTO);
        return "redirect:/admin/registermerchant?success";  // se va tutto bene faccio un redirect mettendo ?success (verrà sempre gestita dal metodo di getmapping /registermerchant di sopra il quale normalmente restituisce l'html registermerchant) in modo che thymeleaf riconosca questo parametro della query e quindi possa mostrare l'avviso di riuscita registrazione
    }


    @Secured({"ROLE_ADMIN"})
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