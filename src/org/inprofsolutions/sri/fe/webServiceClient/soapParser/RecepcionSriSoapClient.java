/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.webServiceClient.soapParser;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.logging.Level;
import javax.xml.datatype.DatatypeConfigurationException;
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
import sri.gob.ec.client.ws.recepcion.Comprobante;
import sri.gob.ec.client.ws.recepcion.Comprobante.Mensajes;
import sri.gob.ec.client.ws.recepcion.Mensaje;
import sri.gob.ec.client.ws.recepcion.ObjectFactory;
import sri.gob.ec.client.ws.recepcion.RespuestaSolicitud;
import sri.gob.ec.client.ws.recepcion.RespuestaSolicitud.Comprobantes;

/**
 *
 * @author caf
 */
public class RecepcionSriSoapClient {
 
    private URL endpoint = null;
    private SOAPConnection connection = null;
    private MessageFactory messageFactory = null;
    
    public RecepcionSriSoapClient(String endpoint) throws SOAPException, MalformedURLException
    {
        this.endpoint = new URL(endpoint); //Revisar
        
//        this.endpoint = new URL(null, endpoint, 
//                new URLStreamHandler() 
//                {
//                    @Override
//                    protected URLConnection openConnection(URL url) throws IOException 
//                    {
//                        URL target = new URL(url.toString());
//                        URLConnection connection = target.openConnection();
//                        // Connection settings
//                        connection.setConnectTimeout(60000); // 60 sec
//                        connection.setReadTimeout(60000); // 60 sec
//                        return(connection);
//                    }
//                }
//        );
        
        connection = SOAPConnectionFactory.newInstance().createConnection();
        messageFactory = MessageFactory.newInstance();
    }
    
    public void closeConnection() throws SOAPException
    {
        connection.close();
    }
    
    public RespuestaSolicitud validarComprobante(byte[] xml, String claveAccesoComprobante) throws SOAPException, DatatypeConfigurationException
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.webServiceClient.soapParser.", "");
        
        SOAPMessage message = messageFactory.createMessage();

        //Obtiene el objeto Envelope asociado al mensaje
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        envelope.setAttribute("xmlns:ec","http://ec.gob.sri.ws.recepcion");
 
        //Obtiene el cuerpo del mesaje contenido en el mensaje SOAP
        SOAPBody body = message.getSOAPBody();
        QName bodyName = new QName("ec:validarComprobante");
        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
        SOAPElement symbol = bodyElement.addChildElement("xml");

        //Se agrega el xml en codificado en base 64
        symbol.addTextNode(org.apache.commons.codec.binary.Base64.encodeBase64String(xml));
        
        try 
        {
            InetAddress address = InetAddress.getByName(endpoint.getHost());
            if(address != null)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,claveAccesoComprobante + " - " +taskID+ " - " + "Host SRI: " + endpoint.getHost() + " (" +address.getHostAddress() + ")");
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,claveAccesoComprobante + " - " +taskID+ " - " + "Host SRI: " + "SRI HOST: " + endpoint.getHost() + "(IP Desconocida)");
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
        
        SOAPElement respuestaSolicitud = ((SOAPElement)((SOAPElement)response.getSOAPBody().getChildElements().next()).getChildElements().next());
        
        ObjectFactory objectFactoryRecepcionWSSRI = new ObjectFactory();
        RespuestaSolicitud respuestaSolicitudWSSRI = objectFactoryRecepcionWSSRI.createRespuestaSolicitud();

        
        respuestaSolicitud.getElementsByTagName("estado").item(0).getTextContent();
        respuestaSolicitudWSSRI.setEstado(respuestaSolicitud.getElementsByTagName("estado").item(0).getTextContent());
        
        Comprobantes comprobantesWSSRI = objectFactoryRecepcionWSSRI.createRespuestaSolicitudComprobantes();
        
        NodeList comprobantesTags = respuestaSolicitud.getElementsByTagName("comprobantes");
        
        if(comprobantesTags.getLength()>0)
        {
            NodeList comprobantes = comprobantesTags.item(0).getChildNodes();
            for(int i = 0 ; i < comprobantes.getLength(); i++)
            {
                Comprobante comprobanteWSSRI = new Comprobante();

                Node comprobante = comprobantes.item(i);
                NodeList infoComprobante = comprobante.getChildNodes(); 
                for(int j = 0; j < infoComprobante.getLength(); j++)
                {
                    if(infoComprobante.item(j).getNodeName() == "claveAcceso")
                    {
                        comprobanteWSSRI.setClaveAcceso(infoComprobante.item(j).getTextContent());
                    }
                    else if(infoComprobante.item(j).getNodeName() == "mensajes")
                    {
                        Mensajes mensajesWSSRI = objectFactoryRecepcionWSSRI.createComprobanteMensajes();

                        NodeList mensajes = infoComprobante.item(j).getChildNodes();
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
                        comprobanteWSSRI.setMensajes(mensajesWSSRI);
                    }
                }
                comprobantesWSSRI.getComprobante().add(comprobanteWSSRI);
            }
            respuestaSolicitudWSSRI.setComprobantes(comprobantesWSSRI);
        }
        return respuestaSolicitudWSSRI;
    }
    
    private static String getStringFromSOAPMessage(SOAPMessage msg) throws SOAPException, IOException {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        msg.writeTo(byteArrayOS);
        return new String(byteArrayOS.toByteArray());
    }
}