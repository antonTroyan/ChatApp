package by.troyan.dao;

import by.troyan.entity.Message;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The interface Chat dao describe main methods that should be implemented
 * by any realization of DAO in this application.
 */
public interface ChatDao {

    /**
     * Save message in db boolean.
     *
     * @param message the message
     * @return true if operation was successful, false in other case
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED,
            rollbackFor = Throwable.class)
    boolean saveMessageInDB(Message message);

    /**
     * Gets list of last messages.
     *
     * @param amount the amount of Message user need to receive
     * @return the list of last messages
     */
    @Transactional(readOnly = true,
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED)
    List<Message> getListOfLastMessages(int amount);
}
