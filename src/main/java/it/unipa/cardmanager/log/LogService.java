package it.unipa.cardmanager.log;

import java.util.List;

public interface LogService {

    void addCardLog(String logType,Long cardId,String info);

    void addMerchantLog(String logType,Long merchantId,String info);

    List<LogDTO> getAllLogs();
}
