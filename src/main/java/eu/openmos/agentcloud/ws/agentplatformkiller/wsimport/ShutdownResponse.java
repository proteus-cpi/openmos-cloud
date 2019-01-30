
package eu.openmos.agentcloud.ws.agentplatformkiller.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per shutdownResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="shutdownResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="shutdownStatus" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shutdownResponse", propOrder = {
    "shutdownStatus"
})
public class ShutdownResponse {

    protected int shutdownStatus;

    /**
     * Recupera il valore della proprietà shutdownStatus.
     * 
     */
    public int getShutdownStatus() {
        return shutdownStatus;
    }

    /**
     * Imposta il valore della proprietà shutdownStatus.
     * 
     */
    public void setShutdownStatus(int value) {
        this.shutdownStatus = value;
    }

}
