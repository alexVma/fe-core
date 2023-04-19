/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.webServiceClient.soapParser;


import sri.gob.ec.client.ws.autorizacion.Autorizacion;
import sri.gob.ec.client.ws.autorizacion.Autorizacion.Mensajes;
import sri.gob.ec.client.ws.autorizacion.Mensaje;
import sri.gob.ec.client.ws.autorizacion.ObjectFactory;
import sri.gob.ec.client.ws.autorizacion.RespuestaComprobante;
import sri.gob.ec.client.ws.autorizacion.RespuestaComprobante.Autorizaciones;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.logging.Level;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author caf
 */
public class AutorizacionSriSoapClient {
 
    private URL endpoint = null;
    private SOAPConnection connection = null;
    private MessageFactory messageFactory = null;
    
    public AutorizacionSriSoapClient(String endpoint) throws SOAPException, MalformedURLException
    {
//        this.endpoint = new URL(endpoint); //Revisar
        
        this.endpoint = new URL(null, endpoint,
                new URLStreamHandler() 
                {
                    @Override
                    protected URLConnection openConnection(URL url) throws IOException 
                    {
                        URL target = new URL(url.toString());
                        URLConnection connection = target.openConnection();
                        // Connection settings
                        connection.setConnectTimeout(60000); // 60 sec
                        connection.setReadTimeout(60000); // 60 sec
                        return(connection);
                    }
                }
        );
        
        connection = SOAPConnectionFactory.newInstance().createConnection();
        messageFactory = MessageFactory.newInstance();
    }
    
    public void closeConnection() throws SOAPException
    {
        connection.close();
    }
    
    public RespuestaComprobante autorizacionComprobante(String claveAccesoComprobante) throws SOAPException, DatatypeConfigurationException
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.webServiceClient.soapParser.", "");
        
        SOAPMessage message = messageFactory.createMessage();
        
        ////Obtiene la  cabecera contenido en el mensaje SOAP
        //SOAPHeader header = message.getSOAPHeader();
        //header.detachNode();
 
        //Obtiene el objeto Envelope asociado al mensaje
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        envelope.setAttribute("xmlns:ec","http://ec.gob.sri.ws.autorizacion");
 
        //Obtiene el cuerpo del mensaje contenido en el mensaje SOAP
        SOAPBody body = message.getSOAPBody();
        QName bodyName = new QName("ec:autorizacionComprobante");
        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
        SOAPElement symbol = bodyElement.addChildElement("claveAccesoComprobante");
        symbol.addTextNode(claveAccesoComprobante);
 
        try 
        {
            InetAddress address = InetAddress.getByName(endpoint.getHost());
            if(address != null)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,claveAccesoComprobante + " - "+taskID+ " - " + "Host SRI: " + endpoint.getHost() + " (" +address.getHostAddress() + ")");
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,claveAccesoComprobante + " - " +taskID+ " - " + "Host SRI: " + endpoint.getHost() + "(IP Desconocida)");
            }
        } 
        catch (Exception e) 
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,claveAccesoComprobante + " - " +taskID+ " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
        
        LoggerContainer.getListLogger().get(0).log(Level.FINEST,claveAccesoComprobante + " - " +taskID+ " - " + "Iniciando la llamada al SRI: " + endpoint);
        SOAPMessage response = connection.call(message, endpoint); //Se envia el mensaje SOAP al servidor y se espera la respuesta
        LoggerContainer.getListLogger().get(0).log(Level.FINEST,claveAccesoComprobante + " - " +taskID+ " - " + "Finalizando la llamada al SRI: " + endpoint);
        
        SOAPBody responseBody = response.getSOAPBody();
        SOAPBodyElement responseElement = (SOAPBodyElement)responseBody.getChildElements().next();
        SOAPElement returnElement = (SOAPElement)responseElement.getChildElements().next();
        
        //Verifica si existio un error
        if(responseBody.getFault()!=null)
        {
            //devuelve el error de existir
            LoggerContainer.getListLogger().get(0).log(Level.INFO,claveAccesoComprobante + " - " + "Fault = SI : " + returnElement.getValue()+" "+responseBody.getFault().getFaultString());
        } 
        else 
        {
            //devuelve null si no existe error
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Fault = NO : " + returnElement.getValue());
        }
 
//        try 
//        {
//            //imprime el mesaje enviado al web service server
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Mensaje enviado = " + getStringFromSOAPMessage(message));
//            
//            //imprime la respuesta devuelta del web service
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Mensaje de respuesta = " + getStringFromSOAPMessage(response));
//            
//        } 
//        catch (IOException e) 
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
//        }
        
        SOAPElement RespuestaAutorizacionComprobante = ((SOAPElement)((SOAPElement)response.getSOAPBody().getChildElements().next()).getChildElements().next());
        
        ObjectFactory objectFactoryAutorizacionWSSRI = new ObjectFactory();
        RespuestaComprobante respuestaComprobanteWSSRI = objectFactoryAutorizacionWSSRI.createRespuestaComprobante();
        
        respuestaComprobanteWSSRI.setClaveAccesoConsultada(RespuestaAutorizacionComprobante.getElementsByTagName("claveAccesoConsultada").item(0).getTextContent());
        respuestaComprobanteWSSRI.setNumeroComprobantes(RespuestaAutorizacionComprobante.getElementsByTagName("numeroComprobantes").item(0).getTextContent());
        
        Autorizaciones autorizacionesWSSRI = objectFactoryAutorizacionWSSRI.createRespuestaComprobanteAutorizaciones();
        
        NodeList autorizacionesTags = RespuestaAutorizacionComprobante.getElementsByTagName("autorizaciones");
        
        if(autorizacionesTags.getLength()>0)
        {
            NodeList autorizaciones = autorizacionesTags.item(0).getChildNodes();
            for(int i = 0 ; i < autorizaciones.getLength(); i++)
            {
                Autorizacion autorizacionWSSRI = new Autorizacion();

                Node autorizacion = autorizaciones.item(i);
                NodeList infoAutorizacion = autorizacion.getChildNodes(); 
                for(int j = 0; j < infoAutorizacion.getLength(); j++)
                {
                    if(infoAutorizacion.item(j).getNodeName() == "estado")
                    {
                        autorizacionWSSRI.setEstado(infoAutorizacion.item(j).getTextContent());
                    }
                    else if(infoAutorizacion.item(j).getNodeName() == "numeroAutorizacion")
                    {
                        autorizacionWSSRI.setNumeroAutorizacion(infoAutorizacion.item(j).getTextContent());
                    }
                    else if(infoAutorizacion.item(j).getNodeName() == "fechaAutorizacion")
                    {
                        autorizacionWSSRI.setFechaAutorizacion(DatatypeFactory.newInstance().newXMLGregorianCalendar(infoAutorizacion.item(j).getTextContent()));
                    }
                    else if(infoAutorizacion.item(j).getNodeName() == "ambiente")
                    {
                        autorizacionWSSRI.setAmbiente(infoAutorizacion.item(j).getTextContent());
                    }
                    else if(infoAutorizacion.item(j).getNodeName() == "comprobante")
                    {
                        autorizacionWSSRI.setComprobante(infoAutorizacion.item(j).getTextContent());

                    }
                    else if(infoAutorizacion.item(j).getNodeName() == "mensajes")
                    {
                        Mensajes mensajesWSSRI = objectFactoryAutorizacionWSSRI.createAutorizacionMensajes();

                        NodeList mensajes = infoAutorizacion.item(j).getChildNodes();
                        for(int k = 0 ; k < mensajes.getLength(); k++)
                        {
                            Mensaje mensajeWSSRI = new Mensaje();

                            Node mensaje = mensajes.item(k);
                            NodeList infoMensaje = mensaje.getChildNodes();
                            for(int l = 0; l < infoMensaje.getLength(); l++)
                            {
                                if(infoMensaje.item(l).getNodeName() == "identificador")
                                {
                                    mensajeWSSRI.setIdentificador(infoMensaje.item(l).getTextContent());
                                }
                                else if(infoMensaje.item(l).getNodeName() == "mensaje")
                                {
                                    mensajeWSSRI.setMensaje(infoMensaje.item(l).getTextContent());
                                }
                                else if(infoMensaje.item(l).getNodeName() == "tipo")
                                {
                                    mensajeWSSRI.setTipo(infoMensaje.item(l).getTextContent());
                                }
                                else if(infoMensaje.item(l).getNodeName() == "informacionAdicional")
                                {
                                    mensajeWSSRI.setInformacionAdicional(infoMensaje.item(l).getTextContent());
                                }
                            }
                            mensajesWSSRI.getMensaje().add(mensajeWSSRI);
                        }
                        autorizacionWSSRI.setMensajes(mensajesWSSRI);
                    }
                }
                autorizacionesWSSRI.getAutorizacion().add(autorizacionWSSRI);
            }
            respuestaComprobanteWSSRI.setAutorizaciones(autorizacionesWSSRI);
        }
        return respuestaComprobanteWSSRI;
    }
    
    private static String getStringFromSOAPMessage(SOAPMessage msg) throws SOAPException, IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        msg.writeTo(byteArrayOS);
        return new String(byteArrayOS.toByteArray());
    }
}