package it.unipa.cardmanager.log;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class LogController {

    private final LogService logService;

    public LogController(LogService logService){
        this.logService = logService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/alllogs")
    public String allTransactions(Model model){
        model.addAttribute(
                "logs",
                this.logService.getAllLogs()
        );
        Map<String,Object> a = model.asMap();
        System.out.println(a.keySet());
        return "admin/alllogs";
    }
}
