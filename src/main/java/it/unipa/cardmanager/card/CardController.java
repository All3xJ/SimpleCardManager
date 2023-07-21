package it.unipa.cardmanager.card;

import it.unipa.cardmanager.transaction.TransactionService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Controller
public class CardController {
    private final CardService cardService;

    private final TransactionService transactionService;

    public CardController(CardService cardService, TransactionService transactionService){
        this.cardService = cardService;
        this.transactionService = transactionService;
    }


    @GetMapping("/search")
    public String searchCard(Model model, @RequestParam(name = "cardId") Long cardNumberToSearch){  // salvo valore del parametro cardId in quella variabile Long
        try {
            CardDTO card = this.cardService.findCardById(cardNumberToSearch);
            model.addAttribute("card", card);
            model.addAttribute("cardSearchOutcome", "ok");  // se è andato tutto bene jquery lato client darà operazione avvenuta con successo
        }catch(NoSuchElementException e){
            model.addAttribute("cardSearchOutcome", "wrongid"); // altrimenti jquery mostrerà errore di carta non trovata
        }catch(Exception e){
            model.addAttribute("cardSearchOutcome", "genericerror");    // oppure un altro errore generico
        }
        return "card";
    }

    @GetMapping("/")
    public String goHome(){
        return "home";
    }

    @GetMapping("/home")
    public String goHome2(){
        return "home";
    }




    @Secured({"ROLE_MERCHANT","ROLE_ADMIN"})
    @GetMapping("/merchant/editcredit")
    public String editCredit(){
        return "merchant/editcredit";
    }

    @Secured({"ROLE_MERCHANT","ROLE_ADMIN"})
    @PostMapping("/merchant/editcredit")
    public ResponseEntity<String> editCredit(@RequestParam(name = "cardId") Long cardNumberToEdit, @RequestParam(name = "amount") Double amount){

        Double newCredit;
        JSONObject jsonObject = new JSONObject();   // creo json
        try {
            newCredit = this.cardService.editCredit(cardNumberToEdit,amount);   // se c'è qualche problema ad es carta è disabilitata, farà throw di una determinata eccezione e jquery lato client farà sì che venga riportato l'errore
            this.transactionService.addTransaction(cardNumberToEdit,amount);    // stesso qui se c'è una qualche eccezione jquery lato client darà errore
            jsonObject.put("result","ok");
            jsonObject.put("newCredit",newCredit);  // se tutto è andato a buon fine inserisco nel json il nuovo credito cosi che in jquery lo mostro
        }catch(NoSuchElementException e){
            jsonObject.put("result",e.getMessage());    // darà "Card not found"
        }catch(IllegalStateException e){
            jsonObject.put("result",e.getMessage());    // darà "Card must be enabled" o "Credit cannot be lower than 0"
        }catch(Exception e){
            jsonObject.put("result", "Generic error");    // oppure un altro errore generico
        }

        return ResponseEntity.ok(jsonObject.toString()); // mi ritrovo il credito nella response che arriva a jquery lato client
    }




    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/createnewcard")
    public String createNewCard(){
        return "admin/createnewcard";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/admin/createnewcard")
    public ResponseEntity<String> createNewCard(@RequestParam(name = "amount") Double amount){
            JSONObject returned = this.cardService.createNewCard(amount);
            return ResponseEntity.ok(returned.toString());  // ritorno json portato a stringa, che verrà interpretato da jquery lato client
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/blockunblockcard")
    public String blockUnblockCard(){
        return "admin/blockunblockcard";
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/admin/blockunblockcard")
    public ResponseEntity<Boolean> blockUnblockCard(@RequestParam(name = "cardId") Long cardId){
        return ResponseEntity.ok(this.cardService.blockUnblockCard(cardId));
    }


    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/cardlist")
    public String getAllCards(Model model){
        List<CardDTO> cards = this.cardService.getAllCards();
        model.addAttribute("cards", cards);
//        Map<String,Object> a = model.asMap();
//        System.out.println(a.keySet());
        // ho commentato perchè l'avevo messo solo per dimostrare che sto model era relativo solo a sta richiesta, non è che conserva cose di altre robe. poi si cancellano sti attributi e si crea altro model in auto
        return "admin/cardlist";
    }
}
