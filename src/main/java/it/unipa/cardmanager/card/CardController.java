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
import java.util.NoSuchElementException;

@Controller // devo mettere controller, altrimenti se metto restcontroller non funziona il ritorno della stringa del templete per thymeleaf
public class CardController {
    private final CardService cardService;

    private final TransactionService transactionService;

    public CardController(CardService cardService, TransactionService transactionService){
        this.cardService = cardService;
        this.transactionService = transactionService;
    }


    @GetMapping("/search")
    public String searchCard(Model model, @RequestParam(name = "cardId") Long cardNumberToSearch){
        try {
            CardDTO card = this.cardService.findCardById(cardNumberToSearch);
            model.addAttribute("card", card);
            model.addAttribute("cardSearchOutcome", "ok");
        }catch(NoSuchElementException e){
            model.addAttribute("cardSearchOutcome", "wrongid");
        }catch(RuntimeException e){
            model.addAttribute("cardSearchOutcome", "genericerror");
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
    public ResponseEntity<Double> editCredit(@RequestParam(name = "cardId") Long cardNumberToEdit, @RequestParam(name = "amount") Double amount){
        Double newCredit = this.cardService.editCredit(cardNumberToEdit,amount);   // se c'è qualche problema ad es carta è disabilitata, fa throw di IllegalStateException
        this.transactionService.addTransaction(cardNumberToEdit,amount);
        return ResponseEntity.ok(newCredit); // invece di tornare la stringa (non posso tornare semplicemente void perche in auto cerchera template che corrisponde al mapping) devo fare sto escamotage per tornare nulla. altrimenti restituisce l'intera pagina html e mi ritrovo questa nella response che arriva a jquery.
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
            return ResponseEntity.ok(returned.toString());
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
    @GetMapping("/admin/cardlist") // sta cosa è ASSURDA. praticamente se metto lo slash alla fine, QUANDO SONO NEL BROWSER DEVOOOOO METTERLOOOOOO. MENTRE SE NON LO METTO, ALLORA NEL BROWSER NOOOOOON DEVO METTERLO. TUTTO QUESTO PERCHÈ ALTRIMENTI "NON TROVA LA RISORSA"
    public String getAllCards(Model model){
        List<CardDTO> cards = this.cardService.getAllCards();
        model.addAttribute("cards", cards);
        return "admin/cardlist";
    }
}
