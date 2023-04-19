/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.Debito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.xml.XMLDocumentGenerator;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.Carpetas;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;
import org.w3c.dom.Document;

/**
 *
 * @author caf
 */
public class XMLW3CGeneratorTask implements Runnable
{
    private QueueDataProcessor queueDataProcessor = null;
    private QueueProcessorThread queueProcessorThread = null;
    
    private XMLDocumentGenerator generador = null;
    
    private String path = null;
    
    public XMLW3CGeneratorTask(QueueDataProcessor queueDataProcessor,Propiedades propiedades, QueueProcessorThread queueProcessorThread) throws ParserConfigurationException
    {
        this.queueDataProcessor = queueDataProcessor;
        this.queueProcessorThread = queueProcessorThread;
        
        this.path = propiedades.leer("PathDirectorio");
        
        generador = new XMLDocumentGenerator();
    }

    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        
        DefaultComprobanteElectronico comprobante = queueDataProcessor.getDefaultComprobanteElectronico();
        
        try
        {
            Document doc = null;

            if(
                    (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura")) ||
                    (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.FacturaExtendida"))
                )
            {
                Factura factura = (Factura)comprobante;
                doc = generador.getFacturaXML(factura);
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"XML Factura Generado");
            }
            else if(
                    (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacion")) ||
                    (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacionExtendida"))
                )
            {
                FacturaExportacion factura = (FacturaExportacion)comprobante;
                doc = generador.getFacturaExportacionXML(factura);
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"XML FacturaExportacion Generado");
            }
            else if(comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion"))
            {
                Retencion retencion = (Retencion)comprobante;
                doc = generador.getRetencionXML(retencion);
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"XML Retencion Generado");
            }
            else if (
                        (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito")) ||
                        (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.CreditoExtendido"))
                    )
            {
                Credito credito = (Credito)comprobante;
                doc = generador.getCreditoXML(credito);
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"XML Credito Generado");
            }
            else if (
                        (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.Debito")) ||
                        (comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.DebitoExtendido"))
                    )
            {
                Debito debito = (Debito)comprobante;
                doc = generador.getDebitoXML(debito);
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"XML Debito Generado");
            }
            else if(comprobante.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision"))
            {
                Remision remision = (Remision)comprobante;
                doc = generador.getRemisionXML(remision);
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"XML Remision Generado");
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"Error: El tipo de objeto de comprobante es diferente a los esperados");
            }
            
            
            File fileCabecera = queueDataProcessor.getFileCabecera();
            if(fileCabecera != null)
            {
                if(fileCabecera.getName().endsWith(".csv"))
                {
                    String nombreCA = "ca-"+fileCabecera.getName().substring(9).replaceFirst(".csv", ".txt");
                    FileWriter fileWriterCA = new FileWriter(new File(path+File.separator+Carpetas.respuestas+File.separator+nombreCA));
                    fileWriterCA.write(((DefaultComprobanteElectronico)comprobante).getClaveAcceso());
                    fileWriterCA.close();
                }
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "fileCabecera no tiene la extension correcta, no se creara el ca-....txt");
                }
                
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " + "fileCabecera es igual a null, no se creara el ca-....txt");
            }
            
            
            queueDataProcessor.setDocumentW3C(doc);
            queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }
}