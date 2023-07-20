package it.unipa.cardmanager.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home(){
        return "home";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/success")
    public void loginPageRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {

//        String role =  authResult.getAuthorities().toString();

//        if(role.contains("ROLE_ADMIN")){
//            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/admin/home"));
//        }
//        else if(role.contains("ROLE_MERCHANT")) {
//            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/merchant/home"));
//        }

        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));  // ho una home unica (dentro la quale mostro con thymeleaf certi elementi in base al ruolo dell'utente loggato), quindi non mi serve reindirizzare a niente in particolare, quindi ho commentato la parte di sopra che inizialmente usavo ma ora non serve più
    }

    @GetMapping("/access_denied")
    public String accessDenied(){
        return "/access_denied";
    }

    @GetMapping("/login_error")
    public String loginError(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);  // prelevo l'eccezione avvenuta al login
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        Map<String,Object> a = model.asMap();
        System.out.println(a.keySet());
        return "login";
    }





    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/registermerchant")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new UserDTO());
        Map<String,Object> a = model.asMap();
        System.out.println(a.keySet());
        return "admin/registermerchant";
    }

    @Secured({"ROLE_ADMIN"})    // gestisce la richiesta post all'url save, dopo che utente fa submit
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
        Map<String,Object> a = model.asMap();
        System.out.println(a.keySet());
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
