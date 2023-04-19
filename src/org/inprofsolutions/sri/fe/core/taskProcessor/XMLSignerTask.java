/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;


import java.util.logging.Level;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.cryptography.CifradorAESCBCPKCS5Padding;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;
import org.inprofsolutions.xml.mityc.signer.XAdESBESSignatureFromDocumentToString;
import org.w3c.dom.Document;

/**
 *
 * @author caf
 */
public class XMLSignerTask implements Runnable 
{
    private QueueDataProcessor queueDataProcessor = null;
    private QueueProcessorThread queueProcessorThread = null;
    
    private XAdESBESSignatureFromDocumentToString signature = null;
    
    private String claveP12 = null;
    private Propiedades propiedades = null;
    /////////
    CifradorAESCBCPKCS5Padding cifrador=null;
    /////////
    public XMLSignerTask(QueueDataProcessor queueDataProcessor,Propiedades propiedades, String claveP12, QueueProcessorThread queueProcessorThread)
    {
        this.queueDataProcessor = queueDataProcessor;
        this.queueProcessorThread = queueProcessorThread;
        
        this.claveP12 = claveP12;
        this.propiedades = propiedades;
        
        ////////////////
        try
        {
        byte[] CLAVE_BYTES = 
            {
                (byte)0x0E, (byte)0x87, (byte)0x45, (byte)0xE6, (byte)0x1A, (byte)0xCE, (byte)0xE4, (byte)0x72, (byte)0x4B, (byte)0xEE, (byte)0x4C, (byte)0x65, (byte)0x6B, (byte)0x2D, (byte)0x1E, (byte)0xCD
            };
            
            byte[] SALT_BYTES = 
            {
                (byte)0xA4, (byte)0xAF, (byte)0x31, (byte)0x09, (byte)0xE3, (byte)0x50, (byte)0xEF, (byte)0x9A, (byte)0x93, (byte)0x2B, (byte)0x1F, (byte)0x9F, (byte)0x11, (byte)0xCC, (byte)0x47, (byte)0x60, (byte)0x8D, (byte)0xA5, (byte)0xCD, (byte)0x5D
            };
            byte[] IV_BYTES = 
            {
                (byte)0x63, (byte)0xA1, (byte)0x11, (byte)0xF6, (byte)0xB1, (byte)0x1D, (byte)0xCF, (byte)0x4F, (byte)0x17, (byte)0x42, (byte)0x78, (byte)0x6D, (byte)0x27, (byte)0x54, (byte)0x26, (byte)0x5B
            };
            cifrador = new CifradorAESCBCPKCS5Padding(new String(CLAVE_BYTES),65536,128,IV_BYTES,SALT_BYTES);
            }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
        ////////////////
        
        
        try
        {
            FimadorDeXMLs();
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
        
        try
        {
            Document doc = queueDataProcessor.getDocumentW3C();
            
            ///////////////////////
            System.out.println(comprobante.getRuc());
            System.out.println(propiedades.leer(comprobante.getRuc()+"PathFirma"));
        signature.PKCS12_RESOURCE = propiedades.leer(comprobante.getRuc()+"PathFirma");
        signature.PKCS12_PASSWORD = cifrador.desencriptar(propiedades.leer(comprobante.getRuc()+"ClaveFirma"));
           ///////////////////////
        System.out.println("XMLSignerTask.java            DOC");
        System.out.println("");
             StreamResult result = new StreamResult(System.out);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
                System.out.println("");
                        System.out.println("");
            
            signature.setDocument(doc);
            signature.execute();
            String stringSigned = signature.getSignedString();
            System.out.println("XMLSignerTask.java            signature.getSignedString()           "+stringSigned);
            queueDataProcessor.setStringSigned(stringSigned);
            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +"XML Firmado");
            queueProcessorThread.getOutData().getData().get(0).getData().add(queueDataProcessor);
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobante.getClaveAcceso() + " - " + taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }
    
    private void FimadorDeXMLs() throws Exception
    {
        signature = new XAdESBESSignatureFromDocumentToString();
        signature.PKCS12_RESOURCE = propiedades.leer("PathFirma");
        signature.PKCS12_PASSWORD = claveP12;
        signature.OUTPUT_DIRECTORY = "";
    }
}
