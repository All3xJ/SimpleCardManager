package it.unipa.cardmanager.log;

import it.unipa.cardmanager.card.Card;
import it.unipa.cardmanager.user.User;
import it.unipa.cardmanager.user.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class LogServiceImpl implements LogService{

    private final LogRepository logRepository;

    private final UserService userService;

    public LogServiceImpl(LogRepository logRepository, @Lazy UserService userService){  // @Lazy per risolvere problema di dipendenza circolare dovuta all'autowiring di Spring. in questo modo mi assicuro che inizializzo prima userService
        this.logRepository = logRepository;
        this.userService = userService;
    }

    @Override
    public void addCardLog(String logType, Long cardId, String info) {  // info di newcard è l'amount, info di blockunblockcard è lo stato true/false
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        String humanDate = formatter.format(date);  // trasformo in stringa
        Log newlog = new Log();
        newlog.setLogType(logType);
        newlog.setCard(new Card()); // creo nuovo oggetto card
        newlog.getCard().setId(cardId); // e setto id della carta in questione
        newlog.setMerchant(null);   // merchant null visto che è un operazione di carta (newcard o blockunblockcard)
        newlog.setAdmin(new User());
        newlog.getAdmin().setId(this.userService.getCurrentUser().getId());  // stesso discorso per adminId che sta aggiungendo questo log
        newlog.setInfo(info);
        newlog.setDateCreated(date);
        this.logRepository.saveAndFlush(newlog);
    }

    @Override
    public void addMerchantLog(String logType, Long merchantId, String info) {  // info di disableenablemerchant è lo stato true/false. non c'è info per registeredmerchant
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        String humanDate = formatter.format(date);  // trasformo in stringa
        Log newlog = new Log();
        newlog.setLogType(logType);
//        newlog.setCard(new Card());
//        newlog.getCard().setId(null);
        newlog.setCard(null);   // commentato quello subito sopra perchè produce errore perchè cardId non può essere null nell'entity Card. quindi faccio direttamente cosi settandolo a null in modo diretto e senza passare da Card
        newlog.setMerchant(new User());
        newlog.getMerchant().setId(merchantId);
        newlog.setAdmin(new User());
        newlog.getAdmin().setId(this.userService.getCurrentUser().getId());  // stesso discorso per adminId che sta aggiungendo questo log
        newlog.setInfo(info);
        newlog.setDateCreated(date);
        this.logRepository.saveAndFlush(newlog);
    }

    private List<LogDTO> convertLogsListToDTO(List<Log> logs){   // trasforma lista di Log a lista di LogDTO
        List<LogDTO> logDTOList = new LinkedList<>();
        for (Log log: logs)
            logDTOList.add(log.toDTO());

        return logDTOList;
    }

    @Override
    public List<LogDTO> getAllLogs() {
        List<LogDTO> sortList = this.convertLogsListToDTO(this.logRepository.findAll());
        sortList.sort((tran1, tran2) -> tran2.getDateCreated().compareTo(tran1.getDateCreated()));  // faccio il sorting dei logs in ordine decrescente di timestamp in modo da visualizzare nell'html prima quelli piu recenti

        return sortList;
    }
}
