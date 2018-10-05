package by.troyan.exception.handler;

import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Used to show information about exceptions in users browser.
 */
@Data
@Builder
@XmlRootElement(name = "ExceptionProperties")
@XmlAccessorType(XmlAccessType.FIELD)
class ExceptionProperties {
    private String status;
    private String errorMessage;
}
