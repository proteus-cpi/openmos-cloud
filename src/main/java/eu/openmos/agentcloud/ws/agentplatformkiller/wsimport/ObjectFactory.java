
package eu.openmos.agentcloud.ws.agentplatformkiller.wsimport;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.openmos.agentcloud.ws.agentplatformkiller.wsimport package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Shutdown_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "shutdown");
    private final static QName _ShutdownResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "shutdownResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.openmos.agentcloud.ws.agentplatformkiller.wsimport
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ShutdownResponse }
     * 
     */
    public ShutdownResponse createShutdownResponse() {
        return new ShutdownResponse();
    }

    /**
     * Create an instance of {@link Shutdown }
     * 
     */
    public Shutdown createShutdown() {
        return new Shutdown();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Shutdown }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "shutdown")
    public JAXBElement<Shutdown> createShutdown(Shutdown value) {
        return new JAXBElement<Shutdown>(_Shutdown_QNAME, Shutdown.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShutdownResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "shutdownResponse")
    public JAXBElement<ShutdownResponse> createShutdownResponse(ShutdownResponse value) {
        return new JAXBElement<ShutdownResponse>(_ShutdownResponse_QNAME, ShutdownResponse.class, null, value);
    }

}
