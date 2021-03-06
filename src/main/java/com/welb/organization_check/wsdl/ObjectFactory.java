
package com.welb.organization_check.wsdl;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.welb.controller.wsdl package.
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.welb.controller.wsdl
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Service }
     *
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link ServiceResponse }
     *
     */
    public ServiceResponse createServiceResponse() {
        return new ServiceResponse();
    }

    /**
     * Create an instance of {@link CallBark }
     *
     */
    public CallBark createCallBark() {
        return new CallBark();
    }

    /**
     * Create an instance of {@link CallBarkResponse }
     *
     */
    public CallBarkResponse createCallBarkResponse() {
        return new CallBarkResponse();
    }

}
