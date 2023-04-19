
package sri.gob.ec.client.ws.autorizacion;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "AutorizacionComprobantes", targetNamespace = "http://ec.gob.sri.ws.autorizacion")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AutorizacionComprobantes {


    /**
     * 
     * @param claveAccesoComprobante
     * @return
     *     returns SRIWebServiceClient.soapui.pruebas.autorizacion.RespuestaComprobante
     */
    @WebMethod
    @WebResult(name = "RespuestaAutorizacionComprobante", targetNamespace = "")
    @RequestWrapper(localName = "autorizacionComprobante", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "SRIWebServiceClient.soapui.pruebas.autorizacion.AutorizacionComprobante")
    @ResponseWrapper(localName = "autorizacionComprobanteResponse", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "SRIWebServiceClient.soapui.pruebas.autorizacion.AutorizacionComprobanteResponse")
    public RespuestaComprobante autorizacionComprobante(
        @WebParam(name = "claveAccesoComprobante", targetNamespace = "")
        String claveAccesoComprobante);

    /**
     * 
     * @param claveAccesoLote
     * @return
     *     returns SRIWebServiceClient.soapui.pruebas.autorizacion.RespuestaLote
     */
    @WebMethod
    @WebResult(name = "RespuestaAutorizacionLote", targetNamespace = "")
    @RequestWrapper(localName = "autorizacionComprobanteLote", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "SRIWebServiceClient.soapui.pruebas.autorizacion.AutorizacionComprobanteLote")
    @ResponseWrapper(localName = "autorizacionComprobanteLoteResponse", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "SRIWebServiceClient.soapui.pruebas.autorizacion.AutorizacionComprobanteLoteResponse")
    public RespuestaLote autorizacionComprobanteLote(
        @WebParam(name = "claveAccesoLote", targetNamespace = "")
        String claveAccesoLote);

}