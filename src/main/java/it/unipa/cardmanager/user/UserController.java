package it.unipa.cardmanager.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
}
