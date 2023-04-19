/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.integrator.webService;

import com.sun.net.httpserver.Headers;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultBaseComprobante;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.Debito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.sri.fe.core.integrator.webService.soapObject.Respuesta;
import org.inprofsolutions.sri.fe.core.taskProcessor.WebServiceRequesterProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.WebServiceResponderProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.sri.fe.webServiceClient.soapParser.AutorizacionSriSoapClient;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;
import sri.gob.ec.client.ws.autorizacion.RespuestaComprobante;


/**
 *
 * @author caf
 */
@WebService() //this binds the SEI to the SIB
public class ProcesarDocumentoImpl implements WebServiceInterface 
{
    QueueProcessorThread webServiceRequesterProcessor = null;
    QueueProcessorThread webServiceResponderProcessor = null;
    
    @Resource
    WebServiceContext webServiceContext;

    public ProcesarDocumentoImpl(QueueProcessorThread webServiceRequesterProcessor, QueueProcessorThread webServiceResponderProcessor) 
    {
        this.webServiceRequesterProcessor = webServiceRequesterProcessor;
        this.webServiceResponderProcessor = webServiceResponderProcessor;
    }
 
    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="Resultado")
    public Respuesta procesarComprobanteFactura(@XmlElement(required=true)@WebParam(name = "Factura") Factura factura) throws Exception
    {
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Llamada al WebService Factura");
//        if(!checkAuthentication())
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Authentication error");
//            throw new Exception("Authentication error");
//        }

        QueueDataProcessor queueDataProcessorNuevo = new QueueDataProcessor();
        queueDataProcessorNuevo.setDefaultComprobanteElectronico(factura);
        
        webServiceRequesterProcessor.getInData().getData().get(0).getData().add(queueDataProcessorNuevo);
        //webServiceRequesterProcessor.addElement(queueDataProcessorNuevo);
        
        DefaultComprobanteElectronico comprobanteActual = (DefaultComprobanteElectronico) factura;
        
        Respuesta respuesta = null;
        while (respuesta == null) 
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteActual.getClaveAcceso() + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
            }
            
            for(int i = 0; i < webServiceResponderProcessor.getInData().getData().get(0).getData().size(); i++)
            {
                QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getInData().getData().get(0).getData().get(i);
                //QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getElement(i);
                
                DefaultComprobanteElectronico comprobanteRevisar = queueDataProcessorRespuesta.getDefaultComprobanteElectronico();
                if(comprobanteRevisar.getCodigoDocumento().equals(comprobanteActual.getCodigoDocumento()))
                {
                    if(comprobanteRevisar.getEstablecimiento().equals(comprobanteActual.getEstablecimiento()))
                    {
                        if(comprobanteRevisar.getPuntoEmision().equals(comprobanteActual.getPuntoEmision()))
                        {
                            if(comprobanteRevisar.getSecuencial().equals(comprobanteActual.getSecuencial()))
                            {
                                respuesta = new Respuesta();
                                respuesta.setRespuestaComprobante(queueDataProcessorRespuesta.getRespuestaComprobante());
                                respuesta.setRespuestaSolicitud(queueDataProcessorRespuesta.getRespuestaSolicitud());
                                webServiceResponderProcessor.getInData().getData().get(0).getData().remove(queueDataProcessorRespuesta);
                                
                                //i = webServiceResponderProcessor.size() + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Devolviendo resultado para WebService Factura");
        return respuesta;
    }

    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="Resultado")
    public Respuesta procesarComprobanteRetencion(@XmlElement(required=true)@WebParam(name = "Retencion") Retencion retencion) throws Exception
    {    
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Llamada al WebService Retencion");
//        if(!checkAuthentication())
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Authentication error");
//            throw new Exception("Authentication error");
//        }
        QueueDataProcessor queueDataProcessorNuevo = new QueueDataProcessor();
        queueDataProcessorNuevo.setDefaultComprobanteElectronico(retencion);
        
        webServiceRequesterProcessor.getInData().getData().get(0).getData().add(queueDataProcessorNuevo);
        //webServiceRequesterProcessor.addElement(queueDataProcessorNuevo);
        
        DefaultComprobanteElectronico comprobanteActual = (DefaultComprobanteElectronico) retencion;
        
        Respuesta respuesta = null;
        while (respuesta == null) 
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteActual.getClaveAcceso() + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
            }
            
            for(int i = 0; i < webServiceResponderProcessor.getInData().getData().get(0).getData().size(); i++)
            {
                QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getInData().getData().get(0).getData().get(i);
                //QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getElement(i);
                                
                DefaultComprobanteElectronico comprobanteRevisar = queueDataProcessorRespuesta.getDefaultComprobanteElectronico();
                if(comprobanteRevisar.getCodigoDocumento().equals(comprobanteActual.getCodigoDocumento()))
                {
                    if(comprobanteRevisar.getEstablecimiento().equals(comprobanteActual.getEstablecimiento()))
                    {
                        if(comprobanteRevisar.getPuntoEmision().equals(comprobanteActual.getPuntoEmision()))
                        {
                            if(comprobanteRevisar.getSecuencial().equals(comprobanteActual.getSecuencial()))
                            {
                                respuesta = new Respuesta();
                                respuesta.setRespuestaComprobante(queueDataProcessorRespuesta.getRespuestaComprobante());
                                respuesta.setRespuestaSolicitud(queueDataProcessorRespuesta.getRespuestaSolicitud());
                                webServiceResponderProcessor.getInData().getData().get(0).getData().remove(queueDataProcessorRespuesta);
                                
                                //i = webServiceResponderProcessor.size() + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Devolviendo resultado para WebService Retencion");
        return respuesta;
    }
    
    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="Resultado")
    public Respuesta procesarComprobanteCredito(@XmlElement(required=true)@WebParam(name = "NotaDeCredito") Credito credito) throws Exception
    {    
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Llamada al WebService Nota de Credito");
//        if(!checkAuthentication())
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Authentication error");
//            throw new Exception("Authentication error");
//        }
        QueueDataProcessor queueDataProcessorNuevo = new QueueDataProcessor();
        queueDataProcessorNuevo.setDefaultComprobanteElectronico(credito);
        
        webServiceRequesterProcessor.getInData().getData().get(0).getData().add(queueDataProcessorNuevo);
        //webServiceRequesterProcessor.addElement(queueDataProcessorNuevo);
        
        DefaultComprobanteElectronico comprobanteActual = (DefaultComprobanteElectronico) credito;
        
        Respuesta respuesta = null;
        while (respuesta == null) 
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteActual.getClaveAcceso() + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
            }
            
            for(int i = 0; i < webServiceResponderProcessor.getInData().getData().get(0).getData().size(); i++)
            {
                QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getInData().getData().get(0).getData().get(i);
                //QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getElement(i);
                                
                DefaultComprobanteElectronico comprobanteRevisar = queueDataProcessorRespuesta.getDefaultComprobanteElectronico();
                if(comprobanteRevisar.getCodigoDocumento().equals(comprobanteActual.getCodigoDocumento()))
                {
                    if(comprobanteRevisar.getEstablecimiento().equals(comprobanteActual.getEstablecimiento()))
                    {
                        if(comprobanteRevisar.getPuntoEmision().equals(comprobanteActual.getPuntoEmision()))
                        {
                            if(comprobanteRevisar.getSecuencial().equals(comprobanteActual.getSecuencial()))
                            {
                                respuesta = new Respuesta();
                                respuesta.setRespuestaComprobante(queueDataProcessorRespuesta.getRespuestaComprobante());
                                respuesta.setRespuestaSolicitud(queueDataProcessorRespuesta.getRespuestaSolicitud());
                                webServiceResponderProcessor.getInData().getData().get(0).getData().remove(queueDataProcessorRespuesta);
                                
                                //i = webServiceResponderProcessor.size() + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Devolviendo resultado para WebService Nota de Credito");
        return respuesta;
    }
    
    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="Resultado")
    public Respuesta procesarComprobanteDebito(@XmlElement(required=true)@WebParam(name = "NotaDeDebito") Debito debito) throws Exception
    {    
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Llamada al WebService Nota de debito");
//        if(!checkAuthentication())
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Authentication error");
//            throw new Exception("Authentication error");
//        }
        
        QueueDataProcessor queueDataProcessorNuevo = new QueueDataProcessor();
        queueDataProcessorNuevo.setDefaultComprobanteElectronico(debito);
        
        webServiceRequesterProcessor.getInData().getData().get(0).getData().add(queueDataProcessorNuevo);
        //webServiceRequesterProcessor.addElement(queueDataProcessorNuevo);
        
        DefaultComprobanteElectronico comprobanteActual = (DefaultComprobanteElectronico) debito;
        
        Respuesta respuesta = null;
        while (respuesta == null) 
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteActual.getClaveAcceso() + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
            }
            
            for(int i = 0; i < webServiceResponderProcessor.getInData().getData().get(0).getData().size(); i++)
            {
                QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getInData().getData().get(0).getData().get(i);
                //QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getElement(i);
                
                DefaultComprobanteElectronico comprobanteRevisar = queueDataProcessorRespuesta.getDefaultComprobanteElectronico();
                if(comprobanteRevisar.getCodigoDocumento().equals(comprobanteActual.getCodigoDocumento()))
                {
                    if(comprobanteRevisar.getEstablecimiento().equals(comprobanteActual.getEstablecimiento()))
                    {
                        if(comprobanteRevisar.getPuntoEmision().equals(comprobanteActual.getPuntoEmision()))
                        {
                            if(comprobanteRevisar.getSecuencial().equals(comprobanteActual.getSecuencial()))
                            {
                                respuesta = new Respuesta();
                                respuesta.setRespuestaComprobante(queueDataProcessorRespuesta.getRespuestaComprobante());
                                respuesta.setRespuestaSolicitud(queueDataProcessorRespuesta.getRespuestaSolicitud());
                                webServiceResponderProcessor.getInData().getData().get(0).getData().remove(queueDataProcessorRespuesta);
                                
                                //i = webServiceResponderProcessor.size() + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Devolviendo resultado para WebService Nota de debito");
        return respuesta;
    }
    
    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="Resultado")
    public Respuesta procesarComprobanteRemision(@XmlElement(required=true)@WebParam(name = "GuiaDeRemision") Remision remision) throws Exception
    {    
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Llamada al WebService Guia de Remision");
//        if(!checkAuthentication())
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Authentication error");
//            throw new Exception("Authentication error");
//        }
                
        QueueDataProcessor queueDataProcessorNuevo = new QueueDataProcessor();
        queueDataProcessorNuevo.setDefaultComprobanteElectronico(remision);
        
        webServiceRequesterProcessor.getInData().getData().get(0).getData().add(queueDataProcessorNuevo);
        //webServiceRequesterProcessor.addElement(queueDataProcessorNuevo);
        
        DefaultComprobanteElectronico comprobanteActual = (DefaultComprobanteElectronico) remision;
        
        Respuesta respuesta = null;
        while (respuesta == null) 
        {
            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteActual.getClaveAcceso() + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
            }
            
            for(int i = 0; i < webServiceResponderProcessor.getInData().getData().get(0).getData().size(); i++)
            {
                QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getInData().getData().get(0).getData().get(i);
                //QueueDataProcessor queueDataProcessorRespuesta = (QueueDataProcessor)webServiceResponderProcessor.getElement(i);
                                
                DefaultComprobanteElectronico comprobanteRevisar = queueDataProcessorRespuesta.getDefaultComprobanteElectronico();
                if(comprobanteRevisar.getCodigoDocumento().equals(comprobanteActual.getCodigoDocumento()))
                {
                    if(comprobanteRevisar.getEstablecimiento().equals(comprobanteActual.getEstablecimiento()))
                    {
                        if(comprobanteRevisar.getPuntoEmision().equals(comprobanteActual.getPuntoEmision()))
                        {
                            if(comprobanteRevisar.getSecuencial().equals(comprobanteActual.getSecuencial()))
                            {
                                respuesta = new Respuesta();
                                respuesta.setRespuestaComprobante(queueDataProcessorRespuesta.getRespuestaComprobante());
                                respuesta.setRespuestaSolicitud(queueDataProcessorRespuesta.getRespuestaSolicitud());
                                webServiceResponderProcessor.getInData().getData().get(0).getData().remove(queueDataProcessorRespuesta);
                                
                                //i = webServiceResponderProcessor.size() + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Devolviendo resultado para WebService Guia de remision");
        return respuesta;
    }

    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="Resultado")
    public Respuesta consultaComprobante(@XmlElement(required=true)@WebParam(name = "Comprobante") DefaultBaseComprobante comprobante) throws SOAPException, MalformedURLException, DatatypeConfigurationException, Exception
    {
        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Llamada al WebService consultaComprobante");
//        if(!checkAuthentication())
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Authentication error");
//            throw new Exception("Authentication error");
//        }
                
        AutorizacionSriSoapClient autorizacionSriSoapClientPruebas  = new AutorizacionSriSoapClient("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl");
        AutorizacionSriSoapClient autorizacionSriSoapClientProduccion  = new AutorizacionSriSoapClient("https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl");
        
        RespuestaComprobante res = null;
        if(comprobante.getCodigoAmbiente().equals("1"))
        {
            res  = autorizacionSriSoapClientPruebas.autorizacionComprobante(comprobante.generarClaveDeAccesoNormal());
        }
        else if(comprobante.getCodigoAmbiente().equals("2"))
        {
            res  = autorizacionSriSoapClientProduccion.autorizacionComprobante(comprobante.generarClaveDeAccesoNormal());
        }
        
        Respuesta respuesta = new Respuesta();
        respuesta.setRespuestaComprobante(res);
        
        return respuesta;
    }
    
    private boolean checkAuthentication()
    {
        MessageContext messageContext = webServiceContext.getMessageContext();
 
	//get detail from request headers
        Map http_headers = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List userList = (List) http_headers.get("Username");
        List passList = (List) http_headers.get("Password");
 
        String username = "";
        String password = "";
 
        if(userList!=null)
        {
            //get username
            username = userList.get(0).toString();
        }
 
        if(passList!=null)
        {
            //get password
            password = passList.get(0).toString();
        }
 
        //Should validate username and password with database
        if (username.equals("0190361020001") && password.equals("0pMg!I5FxP*MksU4iB*O")){
            return true;
        }
        else
        {
            return true;
        }
    }
 }
