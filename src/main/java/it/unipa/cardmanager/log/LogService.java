package it.unipa.cardmanager.log;

import java.util.List;

public interface LogService {

    void addCardLog(String logType,Long cardId,String info);    // non metto admin_id e date perche tanto admin_id lo trovo da dentro e date pure

    void addMerchantLog(String logType,String info);            // same

    List<LogDTO> getAllLogs();
}
