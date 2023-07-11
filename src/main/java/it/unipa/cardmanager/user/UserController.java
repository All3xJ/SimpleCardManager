package it.unipa.cardmanager.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

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

    @GetMapping("/login?error")
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
        return "login";
    }

}
