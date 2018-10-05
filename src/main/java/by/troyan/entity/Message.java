package by.troyan.entity;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

/**
 * Main entity of the application. Holds info about id, content, sender of message, and
 * full LocalDateTime object that will holds lot of data about time message were posted.
 */
@Data
@Builder
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    /** Holds id in long format. Id will set after downloading in DB*/
    private Long id;

    /** Holds content of message. MAX content have 50 chars. MIN 1 char*/
    private String content;

    /** Holds date and time when message was posted and saved in DB*/
    private LocalDateTime date;

    /** Holds nick of user, posted message. MIN length 1 char, MAX 15*/
    private String sender;
}
