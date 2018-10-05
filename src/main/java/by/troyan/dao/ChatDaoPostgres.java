package by.troyan.dao;

import by.troyan.entity.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

/**
 * Realization of the DAO layer. Works with postgres. Also uses row mapper
 * and named parameters jdbc template.
 */
@Repository
public class ChatDaoPostgres implements ChatDao {
    private static final Logger LOG = LogManager.getLogger(ChatDaoPostgres.class);

    private static final String SQL_FOR_SAVE_MSG =
            "INSERT INTO public.messages (sender, content, date) " +
            "VALUES (:sender, :content, :date)";

    private static final String SQL_FOR_GET_MSG =
            "SELECT id, sender, content, date " +
            "FROM public.messages " +
            "ORDER BY id " +
            "DESC " +
            "LIMIT (:amount) ";

    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * NamedParameter Jdbc to simplify work.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public ChatDaoPostgres(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Universal row mapper for message entity. Should be used in
     * future methods that will be appears here.
     */
    private RowMapper<Message> standardRowMapper = (resultSet, i) -> Message.builder()
            .id(resultSet.getLong("id"))
            .sender(resultSet.getString("sender"))
            .content(resultSet.getString("content"))
            .date(resultSet.getTimestamp("date").toLocalDateTime())
            .build();

    @Override
    public boolean saveMessageInDB(Message message) {
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put("sender", message.getSender());
            namedParameters.put("content", message.getContent());
            namedParameters.put("date", Timestamp.valueOf(message.getDate()));
            jdbcTemplate.update(SQL_FOR_SAVE_MSG, namedParameters);
            LOG.info("Message saved in DB!");
            return true;
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }

    @Override
    public List<Message> getListOfLastMessages(int amount) {
        try {
            Map<String, Object> namedParameters = new HashMap<>();
            namedParameters.put("amount", amount);
            List<Message> list = jdbcTemplate.query(SQL_FOR_GET_MSG, namedParameters, standardRowMapper);
            Collections.reverse(list);
            LOG.info(list);
            return list;
        } catch (Exception e) {
            LOG.error(e);
            return new ArrayList<>();
        }
    }
}
