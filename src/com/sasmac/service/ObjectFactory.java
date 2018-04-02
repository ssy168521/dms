
package com.sasmac.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sasmac.service package. 
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

    private final static QName _FileNotFoundException_QNAME = new QName("http://webservices.sasmac.com/", "FileNotFoundException");
    private final static QName _CopyFtoF_QNAME = new QName("http://webservices.sasmac.com/", "CopyFtoF");
    private final static QName _CopyFtoFResponse_QNAME = new QName("http://webservices.sasmac.com/", "CopyFtoFResponse");
    private final static QName _Filetransaction_QNAME = new QName("http://webservices.sasmac.com/", "filetransaction");
    private final static QName _FiletransactionResponse_QNAME = new QName("http://webservices.sasmac.com/", "filetransactionResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sasmac.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FileNotFoundException }
     * 
     */
    public FileNotFoundException createFileNotFoundException() {
        return new FileNotFoundException();
    }

    /**
     * Create an instance of {@link CopyFtoF }
     * 
     */
    public CopyFtoF createCopyFtoF() {
        return new CopyFtoF();
    }

    /**
     * Create an instance of {@link CopyFtoFResponse }
     * 
     */
    public CopyFtoFResponse createCopyFtoFResponse() {
        return new CopyFtoFResponse();
    }

    /**
     * Create an instance of {@link Filetransaction }
     * 
     */
    public Filetransaction createFiletransaction() {
        return new Filetransaction();
    }

    /**
     * Create an instance of {@link FiletransactionResponse }
     * 
     */
    public FiletransactionResponse createFiletransactionResponse() {
        return new FiletransactionResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileNotFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.sasmac.com/", name = "FileNotFoundException")
    public JAXBElement<FileNotFoundException> createFileNotFoundException(FileNotFoundException value) {
        return new JAXBElement<FileNotFoundException>(_FileNotFoundException_QNAME, FileNotFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyFtoF }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.sasmac.com/", name = "CopyFtoF")
    public JAXBElement<CopyFtoF> createCopyFtoF(CopyFtoF value) {
        return new JAXBElement<CopyFtoF>(_CopyFtoF_QNAME, CopyFtoF.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CopyFtoFResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.sasmac.com/", name = "CopyFtoFResponse")
    public JAXBElement<CopyFtoFResponse> createCopyFtoFResponse(CopyFtoFResponse value) {
        return new JAXBElement<CopyFtoFResponse>(_CopyFtoFResponse_QNAME, CopyFtoFResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Filetransaction }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.sasmac.com/", name = "filetransaction")
    public JAXBElement<Filetransaction> createFiletransaction(Filetransaction value) {
        return new JAXBElement<Filetransaction>(_Filetransaction_QNAME, Filetransaction.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FiletransactionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.sasmac.com/", name = "filetransactionResponse")
    public JAXBElement<FiletransactionResponse> createFiletransactionResponse(FiletransactionResponse value) {
        return new JAXBElement<FiletransactionResponse>(_FiletransactionResponse_QNAME, FiletransactionResponse.class, null, value);
    }

}
