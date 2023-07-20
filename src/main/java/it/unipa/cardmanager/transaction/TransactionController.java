package it.unipa.cardmanager.transaction;

import it.unipa.cardmanager.card.CardDTO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @Secured({"ROLE_MERCHANT"})
    @GetMapping("/merchant/merchanttransactions")
    public String merchantTransactions(Model model){
        model.addAttribute(
                "transactions",
                this.transactionService.findTransactionsDoneByMerchantId()
        );
        Map<String,Object> a = model.asMap();
        System.out.println(a.keySet());
        return "merchant/merchanttransactions";
    }

    @Secured({"ROLE_CARDOWNER"})
    @GetMapping("/cardowner/cardmovements")
    public String lastMovements(Model model){
        CardDTO card = this.transactionService.findCardByLoggedOwnerId();
        model.addAttribute("cardId",card.getId());

        model.addAttribute(
                "transactions",
                this.transactionService.findTransactionsByCardId(card.getId())
        );
        Map<String,Object> a = model.asMap();
        System.out.println(a.keySet());
        return "cardowner/cardmovements";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/alltransactions")
    public String allTransactions(Model model){
        model.addAttribute(
                "transactions",
                this.transactionService.getAllTransactions()
        );
        Map<String,Object> a = model.asMap();
        System.out.println(a.keySet());
        return "admin/alltransactions";
    }
}
