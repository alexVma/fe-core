/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.File;
import java.io.FileWriter;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.util.logging.Level;
import javax.xml.soap.SOAPException;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.Carpetas;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.sri.fe.webServiceClient.soapParser.RecepcionSriSoapClient;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;
import sri.gob.ec.client.ws.recepcion.RespuestaSolicitud;

/**
 *
 * @author caf
 */
public class SriRecepcionTask implements Runnable 
{
    private QueueDataProcessor queueDataProcessor = null;
    private QueueProcessorThread queueProcessorThread = null;
    
    private String path = null;
    private RecepcionSriSoapClient recepcionSriSoapClientPruebas = null;
    private RecepcionSriSoapClient recepcionSriSoapClientProduccion = null;
    
    public SriRecepcionTask(QueueDataProcessor queueDataProcessor,Propiedades propiedades, QueueProcessorThread queueProcessorThread)
    {
        System.setProperty("javax.net.ssl.trustStore","security"+File.separator+"jssecacerts.jks");
        System.setProperty("javax.net.ssl.trustStorePassword","changeit");
        
        this.queueDataProcessor = queueDataProcessor;
        this.queueProcessorThread = queueProcessorThread;
        
        this.path = propiedades.leer("PathDirectorio");
        
        try
        {
            ConectarAlSistemaDePruebas();
            ConectarAlSistemaDeProduccion();
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }

    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        
        DefaultComprobanteElectronico comprobante = queueDataProcessor.getDefaultComprobanteElectronico();
        String stringSigned = queueDataProcessor.getStringSigned();
        
        try
        {
            File fileCabecera = queueDataProcessor.getFileCabecera();
            
            RespuestaSolicitud respuesta = null;

            int intentoNumero = 0;
            while (true) 
            {       
                try
                {
                    if(comprobante.getCodigoAmbiente().equals("1"))
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Enviando el documento al sistema de Pruebas de SRI: intentoNumero = " + intentoNumero);
                        respuesta = recepcionSriSoapClientPruebas.validarComprobante(stringSigned.getBytes(),comprobante.getClaveAcceso());
                    }
                    else if (comprobante.getCodigoAmbiente().equals("2"))
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Enviando el documento al sistema de Produccion de SRI: intentoNumero = " + intentoNumero);
                        respuesta = recepcionSriSoapClientProduccion.validarComprobante(stringSigned.getBytes(),comprobante.getClaveAcceso());
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "El codigo ambiente encontrado: " + comprobante.getCodigoAmbiente() + ", no cumple con los requisitos del SRI");
                    }
                    break;
                }
                catch (Exception e)
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
                    if(developer.isDebug())
                    {
                        sleep(1000); //10 segundos Desarrollo
                    }
                    else
                    {
                        sleep(1000); //4 minutos Normal
                    }
                }
                intentoNumero++;
            }

            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Estado: " + respuesta.getEstado());

            if(respuesta.getEstado().equals("DEVUELTA"))
            {
                queueDataProcessor.setRespuestaSolicitud(respuesta);
                
                boolean procesoException = false;

                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Numero de Comprobantes: " + respuesta.getComprobantes().getComprobante().size());
                for(int j = 0 ; j < respuesta.getComprobantes().getComprobante().size() ; j++)
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j);
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Clave de Acceso: " + respuesta.getComprobantes().getComprobante().get(j).getClaveAcceso());
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Numero de Mesajes: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().size());
                    for(int k = 0 ; k < respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().size() ; k++)
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Mensaje k: " + k);
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Identificador: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getIdentificador());
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - InformacionAdicional: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getInformacionAdicional());
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Mensaje: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getMensaje());
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Tipo: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getTipo());
                        
                        if(respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getIdentificador().trim().equals("70"))
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - ERROR: 70 - CLAVE DE ACCESO EN PROCESAMIENTO: Encontrada");
                            if(respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getInformacionAdicional().trim().indexOf(comprobante.getClaveAcceso()) != -1)
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.FINEST,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - La clave de acceso del mensaje es igual a la documento en proceso");
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.WARNING,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - La clave de acceso del mensaje no es igual a la documento en proceso");
                            }
                            
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - Se movera a la cola de Autorizacion");
                            procesoException = true;
                            queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_DEVUELTA);
                            queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);
                        }
                        else if(respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getIdentificador().trim().equals("35"))
                        {   
                            if(respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getInformacionAdicional().trim().equals("Se encontró el siguiente error en la estructura del comprobante: Invalid byte 2 of 2-byte UTF-8 sequence."))
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - ERROR: 35 - ARCHIVO NO CUMPLE ESTRUCTURA XML - Se encontró el siguiente error en la estructura del comprobante: Invalid byte 2 of 2-byte UTF-8 sequence.: Encontrada");
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - ERROR: 35 - Se imprimira el comprobante caracter a caracter para su revision");
                             /*   for(int i = 0 ; i < queueDataProcessor.getStringSigned().length(); i++)
                                {
                                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - " + queueDataProcessor.getStringSigned().charAt(i) + " " + ((int)queueDataProcessor.getStringSigned().charAt(i)));
                                }*/
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "Comprobante j: " + j + " - ERROR: 35 - No se pudo determinar el tipo de error");
                            }
                        }
                    }
                }
                
                if(!procesoException)
                {
                    if(queueDataProcessor.getTipoIntegracion() == null)
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Error tipo de integracion null, ejecucion del documento rota.");
                    }
                    else if(queueDataProcessor.getTipoIntegracion().equals("CSV") || queueDataProcessor.getTipoIntegracion().equals("XMLSF"))
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
                                fileWriter.write(respuesta.getEstado()+ "\r\n");
                                for(int j = 0 ; j < respuesta.getComprobantes().getComprobante().size() ; j++)
                                {
                                    for(int k = 0 ; k < respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().size() ; k++)
                                    {
                                        fileWriter.write("Identificador: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getIdentificador()+ "\r\n");
                                        fileWriter.write("InformacionAdicional: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getInformacionAdicional()+ "\r\n");
                                        fileWriter.write("Mensaje: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getMensaje()+ "\r\n");
                                        fileWriter.write("Tipo: " + respuesta.getComprobantes().getComprobante().get(j).getMensajes().getMensaje().get(k).getTipo()+ "\r\n");
                                    }
                                }
                                fileWriter.close(); 
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "fileCabecera no tiene la extension correcta, no se creara el resultado-....txt");
                            }
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "fileCabecera es igual a null, no se creara el resultado-....txt");
                        }

                        if(fileCabecera != null)
                        {
                            if(fileCabecera.getName().endsWith(".csv"))
                            {
                                boolean procesado = fileMove(new File []{new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+fileCabecera.getName()),new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+queueDataProcessor.getFileDetalle().getName())}, path+File.separator+Carpetas.ficheroscsvprocesados,comprobante, taskID);
                                if(procesado)
                                {
                                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Documento " + fileCabecera.getName().substring(9).replaceFirst(".csv", "") + " se ha procesado");
                                }
                            }
                            else if(fileCabecera.getName().endsWith(".xml"))
                            {
                                boolean procesadoCabecera = fileMove(new File []{new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+fileCabecera.getName())}, path+File.separator+Carpetas.ficheroscsvprocesados,comprobante, taskID);
                                if(procesadoCabecera)
                                {
                                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Documento " + fileCabecera.getName() + " se ha procesado");
                                }
                            }                                     
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Archivo: " + fileCabecera.getName() + " no se pudo determinar la extension del archivo, no se movera ningun archivo");
                            }
                        }
                        else
                        {
                            queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_DEVUELTA);
                            queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);

                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Filecabecera es null - no se movera ningun archivo");
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Documento " + (queueDataProcessor.getDefaultComprobanteElectronico()).getClaveAcceso() + " se ha procesado");
                        }

                    }
                    else if(queueDataProcessor.getTipoIntegracion().equals("DBSQL"))
                    {
                        queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_DEVUELTA);
                        queueProcessorThread.getOutData().getData().get(1).getData().add(queueDataProcessor);
                        
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Documento " + (queueDataProcessor.getDefaultComprobanteElectronico()).getClaveAcceso() + " se ha procesado");
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Error tipo de integracion desconocida, ejecucion del documento rota.");
                    }

                    //Imprime el XML Firmado, devuelto por el SRI
                  ////////////////////alexv
                   // LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +stringSigned);
                 
                  // queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_RECIBIDA); ///alex_v todo esto xq no estoy seguro como se manejan los hilos aqui asi q lo vuelvo a enviar 
                                                                                                   //hago lo mismo en la linea 143 del sriAutorizacionTask y ahi si funciona esto  
                queueDataProcessor.getDefaultComprobanteElectronico().setCodigoAmbiente("421");
                queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);
                /////////////////// alexv
                }
            }
            else
            {
                queueDataProcessor.setEstadoDocumento(QueueDataProcessor.DOCUMENTO_RECIBIDA);
                queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);
            }
        }
        catch(Exception e)
        {   e.printStackTrace();
            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
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
    
    private void ConectarAlSistemaDePruebas() throws SOAPException, MalformedURLException
    {
        recepcionSriSoapClientPruebas = new RecepcionSriSoapClient("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl");
    }

    private void ConectarAlSistemaDeProduccion() throws SOAPException, MalformedURLException
    {
        recepcionSriSoapClientProduccion = new RecepcionSriSoapClient("https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl");
    }
}