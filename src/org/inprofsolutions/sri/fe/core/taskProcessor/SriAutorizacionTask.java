/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPException;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.Debito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.pdfRide.PdfGenerator;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.xml.XMLDocumentGenerator;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.Carpetas;
import org.inprofsolutions.sri.fe.core.integrator.webService.soapObject.Respuesta;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.sri.fe.webServiceClient.soapParser.AutorizacionSriSoapClient;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;
import sri.gob.ec.client.ws.autorizacion.RespuestaComprobante;
import sri.gob.ec.client.ws.autorizacion.RespuestaComprobante.Autorizaciones;

/**
 *
 * @author caf
 */
public class SriAutorizacionTask implements Runnable 
{
    private QueueDataProcessor queueDataProcessor = null;
    private QueueProcessorThread queueProcessorThread = null;
    
    private AutorizacionSriSoapClient autorizacionSriSoapClientPruebas  = null;
    private AutorizacionSriSoapClient autorizacionSriSoapClientProduccion  = null;
    
    private SriRecepcionProcessor recepcionSriProcessor = null;
    
    private PdfGenerator pdfGenerator = null;
    private byte [] logobyte = null;
    private byte [] logoSubsidiobyte = null;
    
    private Propiedades propiedades = null; 
    private String path = null;
    
    public SriAutorizacionTask(QueueDataProcessor queueDataProcessor, Propiedades propiedades, QueueProcessorThread queueProcessorThread, SriRecepcionProcessor recepcionSriProcessor,String tipoEndPage) throws IOException 
    {
        System.setProperty("javax.net.ssl.trustStore","security"+File.separator+"jssecacerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","changeit");
        
        this.queueDataProcessor = queueDataProcessor;
        this.queueProcessorThread = queueProcessorThread;
        
        this.propiedades = propiedades;
        
        this.path = propiedades.leer("PathDirectorio");
        
        try
        {
            ConectarAlSistemaDePruebas();
            ConectarAlSistemaDeProduccion();
            //LoadLogoTipo();//alexv ya no inicio la cargada del logo al iniciar la clase si no q va a cargar cada vez q se le pase el ruc
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
        
        GeneradorDePDFs(tipoEndPage);
        
        this.recepcionSriProcessor = recepcionSriProcessor;
    }
    
    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        DefaultComprobanteElectronico comprobanteElectronico = null;
        
        try
        {
            comprobanteElectronico = queueDataProcessor.getDefaultComprobanteElectronico();
            
            File fileCabecera = queueDataProcessor.getFileCabecera();
            
            //String stringSigned = queueDataProcessor.getStringSigned();

            RespuestaComprobante res  = null;
            int intentoNumero = 0;

            sleep(1000);
            
            Thread timerReintento = new Thread()
            {

                @Override
                public void run() 
                {
                    try 
                    {    
                        if(developer.isDebug())
                        {
                            sleep(1000); //1 minutos Desarrollo
                        }
                        else
                        {
                            sleep(1000); //10 minutos Normal
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SriAutorizacionTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            timerReintento.start();
            
            while (true) 
            {
                try
                {
                    if(comprobanteElectronico.getCodigoAmbiente().equals("1"))
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"Chequeando Autorizacion en el sistema de Pruebas del SRI: intentoNumero = " + intentoNumero);
                        res  = autorizacionSriSoapClientPruebas.autorizacionComprobante(comprobanteElectronico.getClaveAcceso());
                    }
                    else if(comprobanteElectronico.getCodigoAmbiente().equals("2"))
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"Chequeando Autorizacion en el sistema de Produccion del SRI: intentoNumero = " + intentoNumero);
                        res  = autorizacionSriSoapClientProduccion.autorizacionComprobante(comprobanteElectronico.getClaveAcceso());
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "El codigo ambiente encontrado: " + comprobanteElectronico.getCodigoAmbiente() + ", no cumple con los requisitos del SRI");
                        
                        
                        ////alex_v
                        queueDataProcessor.setRespuestaComprobante(null);
                        queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_NO_AUTORIZADO);
                        queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);
                        
                        
                        ////alex_v
                        return;
                    }

                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"N. de Autorizaciones: " + res.getAutorizaciones().getAutorizacion().size());

                    if(res.getAutorizaciones().getAutorizacion().isEmpty())
                    {
                        if(!timerReintento.isAlive())
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"El timerReintento ha alcanzado el tiempo maximo, se enviara nuevamente el comprobante");
                            recepcionSriProcessor.getInData().getData().get(0).getData().add(queueDataProcessor);
                            return;
                        }

                        if(developer.isDebug())
                        {
                            sleep(1000); //5 segundos Desarrollo
                        }
                        else
                        {
                            sleep(1000); //45 segundos Normal
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                catch(Exception e)
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}

                    if(developer.isDebug())
                    {
                        sleep(1000); //10 segundos Desarrollo
                    }
                    else
                    {
                        sleep(1000); //5 minutos Normal
                    }
                }
                intentoNumero++;
            }

            //int j = 0;
            for(int j = 0 ; j < res.getAutorizaciones().getAutorizacion().size() ; j++)
            {    
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID  + " - " + "Comprobante j: " + j);

                if(res.getAutorizaciones().getAutorizacion().get(j).getEstado().equals("AUTORIZADO"))
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Ambiente: " + res.getAutorizaciones().getAutorizacion().get(j).getAmbiente()+ "; Estado: " + res.getAutorizaciones().getAutorizacion().get(j).getEstado()+ "; NumeroDeAutorizacion: " + res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion() + "; FechaAutorizacion: " + res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion());

                    if(queueDataProcessor.getStringSigned().trim().equals(res.getAutorizaciones().getAutorizacion().get(j).getComprobante().trim()))
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.FINEST,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - El comprobante enviado y el recibido son los mismos");
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.WARNING,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - El comprobante enviado y el recibido NO son los mismos");
                    }
                    
                    queueDataProcessor.setRespuestaComprobante(res);
                    queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_AUTORIZADO);

                    retornarResultado(fileCabecera, res, comprobanteElectronico, j, taskID);

                    XMLDocumentGenerator xMLDocumentGenerator = new XMLDocumentGenerator();
                    queueDataProcessor.setStringSigned(xMLDocumentGenerator.getXMLAutorizacionSRI(res.getAutorizaciones().getAutorizacion().get(j).getEstado(), res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion(),res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion().toString(),res.getAutorizaciones().getAutorizacion().get(j).getAmbiente(),res.getAutorizaciones().getAutorizacion().get(j).getComprobante()));
                    
                    if(propiedades.leer("GuardarXML").equals("Si"))
                    {
                        String pathAux=path+
                                            File.separator+
                                            Carpetas.autorizados+
                                            File.separator;
                            Date auxDate=new Date();
                            String anio=""+(auxDate.getYear()+1900);
                            String mes=""+(auxDate.getMonth()+1);
                            
                            File fAnio =new File(pathAux+anio+File.separator);
                            File fMes =new File(pathAux+anio+File.separator+mes+File.separator);
                            
                            try{
                                if(!fAnio.exists()){
                                    fAnio.mkdir();
                                    fAnio.mkdirs();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            
                            try{
                                if(!fMes.exists()){
                                    fMes.mkdir();
                                    fMes.mkdirs();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        
                        java.io.FileOutputStream out = new java.io.FileOutputStream(
                                new File(pathAux+
                                          //  anio+File.separator+mes+File.separator+
                                org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento())+"_"+comprobanteElectronico.getEstablecimiento()+"-"+comprobanteElectronico.getPuntoEmision()+"-"+comprobanteElectronico.getSecuencial()+".xml"));
                        out.write(queueDataProcessor.getStringSigned().getBytes("UTF-8"));
                        out.close();
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - La opcion de guardar XML esta deshabilitada");
                    }

                    byte [] pdfByte = null;
                    if(
                            (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura")) ||
                            (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.FacturaExtendida"))
                        )
                    {
                        Factura factura = (Factura)comprobanteElectronico;
                        LoadLogoTipo(factura.getRuc());
                        pdfByte = pdfGenerator.getFactura(factura,res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion(),res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion(),logobyte,logoSubsidiobyte);
                        //pdfByte = pdfGenerator.getFactura(factura,null,"",logobyte);
                    }
                    else if(
                            (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacion")) ||
                            (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacionExtendida"))
                        )
                    {
                        FacturaExportacion facturaExportacion = (FacturaExportacion)comprobanteElectronico;
                        LoadLogoTipo(facturaExportacion.getRuc());
                        pdfByte = pdfGenerator.getFacturaExportacion(facturaExportacion,res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion(),res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion(),logobyte);
                        //pdfByte = pdfGenerator.getFactura(factura,null,"",logobyte);
                    }
                    else if(comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion"))
                    {
                        Retencion retencion = (Retencion)comprobanteElectronico;
                         LoadLogoTipo(retencion.getRuc());
                        pdfByte = pdfGenerator.getRetencion(retencion,res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion(),res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion(),logobyte);
                        //pdfByte = pdfGenerator.getRetencion(retencion,null,"",logobyte);
                    }
                    else if (
                                (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito")) ||
                                (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.CreditoExtendido"))
                            )
                    {
                        Credito credito = (Credito)comprobanteElectronico;
                        LoadLogoTipo(credito.getRuc());
                        pdfByte = pdfGenerator.getCredito(credito,res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion(),res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion(),logobyte);
                        //pdfByte = pdfGenerator.getCredito(credito,null,"",logobyte);
                    }
                    else if (
                                (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.Debito")) ||
                                (comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.DebitoExtendido"))
                            )
                    {
                        Debito debito = (Debito)comprobanteElectronico;
                         LoadLogoTipo(debito.getRuc());
                        pdfByte = pdfGenerator.getDebito(debito,res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion(),res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion(),logobyte);
                        //pdfByte = pdfGenerator.getDebito(debito,null,"",logobyte);
                    }
                    else if(comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision"))
                    {
                        Remision remision = (Remision)comprobanteElectronico;
                        LoadLogoTipo(remision.getRuc());
                        pdfByte = pdfGenerator.getGuiaDeRemision(remision,res.getAutorizaciones().getAutorizacion().get(j).getFechaAutorizacion(),res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion(),logobyte);
                        //pdfByte = pdfGenerator.getGuiaDeRemision(remision,null,"",logobyte);
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.SEVERE,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Error: El tipo de objeto (" + comprobanteElectronico.getClass().getName() + ") de comprobante es diferente a los esperados");
                    }

                    if(propiedades.leer("GuardarPDF").equals("Si"))
                    {
                        if(pdfByte != null)
                        {
                            String pathAux=path+
                                            File.separator+
                                            Carpetas.autorizados+
                                            File.separator;
                            Date auxDate=new Date();
                            String anio=""+(auxDate.getYear()+1900);
                            String mes=""+(auxDate.getMonth()+1);
                            
                            File fAnio =new File(pathAux+anio+File.separator);
                            File fMes =new File(pathAux+anio+File.separator+mes+File.separator);
                            
                            try{
                                if(!fAnio.exists()){
                                    fAnio.mkdir();
                                    fAnio.mkdirs();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            
                            try{
                                if(!fMes.exists()){
                                    fMes.mkdir();
                                    fMes.mkdirs();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            
                            java.io.FileOutputStream out = new java.io.FileOutputStream(
                                    new File(pathAux+
                                            //+anio+File.separator+mes+File.separator+
                                            org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento())+"_"+comprobanteElectronico.getEstablecimiento()+"-"+comprobanteElectronico.getPuntoEmision()+"-"+comprobanteElectronico.getSecuencial()+".pdf"));
                            out.write(pdfByte);
                            out.close();
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Error: El pdfByte es null por lo cual no sera guardado");
                        }
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - La opcion de guardar PDF esta deshabilitada");
                    }

                    if(propiedades.leer("EnviarCorreo").equals("Si"))
                    {
                        //llamar el procesador de correo
                        queueDataProcessor.setPdfByte(pdfByte);

                        queueProcessorThread.getOutData().getData().get(1).getData().add(queueDataProcessor);
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - La opcion de enviar correo esta deshabilitada");
                    }
                    break;
                }
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID  + " - " + "Comprobante j: " + j + " - Comprobante NO AUTORIZADO");
                }

                if(j == res.getAutorizaciones().getAutorizacion().size()-1)
                {
                    j=0;
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"No se ha encontrado ningun comprobante Autorizado, Se imprimira el Comprobante mas actual encontrado j: " + j);

                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Numero de Mesajes: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().size());
                    for(int k = 0 ; k < res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().size() ; k++)
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Mensaje k: " + k);
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Identificador: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getIdentificador());
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - InformacionAdicional: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getInformacionAdicional());
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Mensaje: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getMensaje());
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Tipo: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getTipo());
                        
                        if(res.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje().get(k).getIdentificador().trim().equals("39"))
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - ERROR: 39 - La firma es invalida [Firma inválida (firma y/o certificados alterados)]: Encontrada");
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - ERROR: 39 - Se imprimira el comprobante caracter a caracter para su revision");
                           /* for(int i = 0 ; i < queueDataProcessor.getStringSigned().length(); i++)
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + queueDataProcessor.getStringSigned().charAt(i) + " " + ((int)queueDataProcessor.getStringSigned().charAt(i)));
                            }*/

                        }
                        
                    }

                    queueDataProcessor.setRespuestaComprobante(res);
                    queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_NO_AUTORIZADO);

                    retornarResultado(fileCabecera, res, comprobanteElectronico, j, taskID);

                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - " + queueDataProcessor.getStringSigned());
                    break;
                }
            }

            if(fileCabecera != null)
            {
                if(fileCabecera.getName().endsWith(".csv"))
                {
                    boolean procesado = fileMove(new File []{new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+fileCabecera.getName()),new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+queueDataProcessor.getFileDetalle().getName())}, path+File.separator+Carpetas.ficheroscsvprocesados,comprobanteElectronico, taskID);
                    if(procesado)
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"Documento " + fileCabecera.getName().substring(9).replaceFirst(".csv", "") + " se ha procesado");
                    }
                }
                else if(fileCabecera.getName().endsWith(".xml"))
                {
                    boolean procesadoCabecera = fileMove(new File []{new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+fileCabecera.getName())}, path+File.separator+Carpetas.ficheroscsvprocesados,comprobanteElectronico, taskID);
                    if(procesadoCabecera)
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"Documento " + fileCabecera.getName().substring(9).replaceFirst(".csv", "") + " se ha procesado");
                    }
                }                                     
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"Archivo: " + fileCabecera.getName() + " no se pudo determinar la extension del archivo, no se movera ningun archivo");
                }
            }
            else
            {
                queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);

                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"Filecabecera es null - no se movera ningun archivo");
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +"Documento " + (queueDataProcessor.getDefaultComprobanteElectronico()).getClaveAcceso() + " se ha procesado");
            }
        }
        catch(Exception ex)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " +ex.toString());if(developer.isDebug()){ex.printStackTrace();}
        }
        
    }
    
    private void LoadLogoTipo(String queLeer) throws FileNotFoundException, IOException
    {
        //FileInputStream logofip = new FileInputStream(propiedades.leer("PathLogotipo"));
        FileInputStream logofip = new FileInputStream(propiedades.leer(queLeer));
        logobyte = new byte [logofip.available()];
        logofip.read(logobyte);
        
        FileInputStream logofip2 = new FileInputStream(propiedades.leer("PathLogoSub"));
        logoSubsidiobyte = new byte [logofip2.available()];
        logofip2.read(logoSubsidiobyte);
    }
    
    private void ConectarAlSistemaDePruebas() throws SOAPException, MalformedURLException
    {
        autorizacionSriSoapClientPruebas = new AutorizacionSriSoapClient("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl");
    }

    private void ConectarAlSistemaDeProduccion() throws SOAPException, MalformedURLException
    {
        autorizacionSriSoapClientProduccion = new AutorizacionSriSoapClient("https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl");
    }
    
    private void GeneradorDePDFs(String tipoEndPage)
    {
        pdfGenerator = new PdfGenerator(tipoEndPage);
    }
    
    public boolean fileMove(File [] archivos,String newPath,DefaultComprobanteElectronico comprobanteElectronico, String taskID)
    {
        try
        {
            for(int i = 0; i < archivos.length;i++)
            {             
                if(archivos[i].renameTo(new File(newPath+File.separator + archivos[i].getName())))
                {
                     LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Se movio el archivo: " + newPath+File.separator + archivos[i].getName());
                }
                else
                {
                     LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "No se puede mover el archivo: " + archivos[i].getName() + " al Directorio: " + newPath);
                     return false;
                }
            }
    	}
        catch(Exception e)
        {
    		LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
    	}
        return true;
    }
    
    public void retornarResultado(File fileCabecera, RespuestaComprobante res, DefaultComprobanteElectronico comprobanteElectronico, int j, String taskID) throws IOException
    {
        String nombreLog = null;
        if(fileCabecera != null)
        {
            if(fileCabecera.getName().endsWith(".csv") || fileCabecera.getName().endsWith(".xml"))
            {
                if(fileCabecera.getName().endsWith(".csv"))
                {
                    nombreLog = "resultado-" + fileCabecera.getName().substring(9).replaceFirst(".csv", ".txt");
                }
                else if(fileCabecera.getName().endsWith(".xml"))
                {
                    nombreLog = "resultado-" + fileCabecera.getName().replaceFirst(".xml", ".txt");
                }

                FileWriter fileWriter = new FileWriter(new File(path+File.separator+Carpetas.respuestas+File.separator+nombreLog));
                fileWriter.write("RECIBIDA"+ "\r\n");
                fileWriter.write(res.getAutorizaciones().getAutorizacion().get(j).getEstado()+ "\r\n");
                fileWriter.write(res.getAutorizaciones().getAutorizacion().get(j).getAmbiente()+ "\r\n");
                fileWriter.write(res.getClaveAccesoConsultada()+ "\r\n");
                
                if(res.getAutorizaciones().getAutorizacion().get(j).getEstado().equals("AUTORIZADO"))
                {
                    fileWriter.write(res.getAutorizaciones().getAutorizacion().get(j).getNumeroAutorizacion()+ "\r\n");
                }
                else
                {
                    for(int k = 0 ; k < res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().size() ; k++)
                    {
                        fileWriter.write("Identificador: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getIdentificador()+ "\r\n");
                        fileWriter.write("InformacionAdicional: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getInformacionAdicional()+ "\r\n");
                        fileWriter.write("Mensaje: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getMensaje()+ "\r\n");
                        fileWriter.write("Tipo: " + res.getAutorizaciones().getAutorizacion().get(j).getMensajes().getMensaje().get(k).getTipo()+ "\r\n");
                    }
                }
                fileWriter.close();
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - fileCabecera no tiene la extension correcta, no se creara el resultado-....txt");
            }
        }
        else
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - fileCabecera es igual a null, no se creara el resultado-....txt");
        }
    }
}