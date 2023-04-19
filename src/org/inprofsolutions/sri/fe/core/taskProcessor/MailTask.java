/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.mail.MailSender;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;

/**
 *
 * @author caf
 */
public class MailTask implements Runnable 
{
    private String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
    
    private String stringSigned = null;
    private DefaultComprobanteElectronico comprobanteElectronico = null;
    private byte [] pdfByte = null;
    private MailSender mail = null;
    private String asuntoCorreo = null;
    private String mensajeCorreo = null;
    
    public MailTask(QueueDataProcessor queueDataProcessor, MailSender mail, String asuntoCorreo, String mensajeCorreo) 
    {
        this.stringSigned = queueDataProcessor.getStringSigned();
        this.comprobanteElectronico = queueDataProcessor.getDefaultComprobanteElectronico();
        this.pdfByte = queueDataProcessor.getPdfByte();
        this.mail = mail;
        
        this.asuntoCorreo = asuntoCorreo;
        
        this.mensajeCorreo = mensajeCorreo;
                        
////        "Estimado cliente,"                
////        + "\nAdjunto encontrara su "+org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento())+" electronica"
////        + "\nSaludos.";
//
//        "Estimado(a) cliente"
//        +"\nPresente.-"
//        +"\nUsted ha recibido su factura electrónica¹ a través de este medio."
//        +"\nAdjunto sírvase encontrar el detalle² de sus compras en Indumot."
//        +"\nGracias por preferirnos."
//        +"\nAtentamente,"
//        +"\nINDUMOT"
//        +"\n\n_________________________________________________________________"
//        +"\n¹ La factura electrónica es el archivo XML adjunto, le solicitamos que lo almacene de manera segura puesto que tiene validez tributaria."
//        +"\n² La representación impresa de la factura electrónica es el archivo PDF adjunto, no es necesario que la imprima.";
    }

    @Override
    public void run() 
    {
        while(true)
        {
            try
            {
                if(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().size() == 0)
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "Campos adicionales = 0; No existe direccion de correo destino, no se enviara el correo.");
                    break;
                }
                
//              1) Debe tener un solo caracter @ que no debe ser el 1er ni el ultimo caracter del Email.
//              2) el "punto" no puede ser el 1er ni el ultimo carácter del nombre de cuenta ni de dominio.
//              3) el dominio debe tener por lo menos un "punto".
//              4) No pueden haber dos o más "puntos" juntos.
//              5) Ningún espacio en blanco en todo el nombre del Email. 
//              6) Se debe poder guardar el email como se escribe respetando las mayusculas y minusculas 
//                 ya que el 0.01% de los caso se valida esta condicion.
                
                String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                Pattern pattern = Pattern.compile(PATTERN_EMAIL);
                String destinatarios = "";
                for(int i = 0; i < comprobanteElectronico.getInfoAdicional().getCamposAdicionales().size() ; i++)
                {
                    if(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getNombre().toLowerCase().equals("email"))
                    {
                        Matcher matcher = pattern.matcher(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor());
                     /*   if(!matcher.matches())
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " +taskID + " - " +"No se enviara el correo a la direccion destino " + comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor() + " esta es incorrecta");
                        }
                        else
                        {*/
                            destinatarios = destinatarios + comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor() + ", ";
                        //}
                    }
                }
                
                if(destinatarios.length() == 0)
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "No existe direccion de correo destino, no se enviara el correo.");
                    break;
                }
                
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " +taskID + " - " +"El correo electronico se esta enviando...");
                
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " +taskID + " - " +"Destinatarios To: " + destinatarios);
                mail.SendAMail(destinatarios,null,null,asuntoCorreo,mensajeCorreo,false, 
                        new String[] {
                            "RIDE_" + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento())+"_"+comprobanteElectronico.getEstablecimiento()+"-"+comprobanteElectronico.getPuntoEmision()+"-"+comprobanteElectronico.getSecuencial()+".pdf",
                            org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento())+"_"+comprobanteElectronico.getEstablecimiento()+"-"+comprobanteElectronico.getPuntoEmision()+"-"+comprobanteElectronico.getSecuencial()+".xml"
                        },
                        new byte [][]{pdfByte,stringSigned.getBytes()},
                        new String[] {"application/pdf","text/xml"});
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + "El correo electronico ha sido enviado");
                break;
            }
            catch (Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + taskID + " - " + e.toString());if(developer.isDebug()){e.printStackTrace();}
            }
        }
    }
    
}
