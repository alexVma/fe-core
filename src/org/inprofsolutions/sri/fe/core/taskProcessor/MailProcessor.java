/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.FileInputStream;
import java.util.logging.Level;

import javax.mail.NoSuchProviderException;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.mail.MailSender;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.cryptography.base.Cifrador;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class MailProcessor extends QueueProcessorThread
{
    
    private MailSender mail = null;
    private Propiedades propiedades = null;
    private Cifrador cifrador = null;
    private String asuntoCorreo = null;
    private String mensajeCorreo = null;
    
    public MailProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize, Propiedades propiedades, Cifrador cifrador) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        
        this.propiedades = propiedades;
        this.cifrador = cifrador;
        
        asuntoCorreo = this.propiedades.getProperty("CorreoAsunto");
        
        try
        {
            FileInputStream fis = new FileInputStream("mensajeMail.mailconf");
            byte [] mensajeMail = new byte[fis.available()];
            fis.read(mensajeMail);
            mensajeCorreo = new String(mensajeMail,"UTF-8");
        }
        catch(Exception e)
        {
            System.out.println(e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }

    @Override
    public void run() 
    {
        try
        {
            ConectarAlMailServer();
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Error: al Iniciar el correo");
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
        
        while (true) 
        {
            try
            {
                
                if(getInData().getData().get(0).getData().size() > 0)
                {
                    do 
                    {
                        QueueDataProcessor queueDataProcessor = (QueueDataProcessor)getInData().getData().get(0).getData().get(0);
                        
                        addTaskToQueue(new MailTask(queueDataProcessor, mail, asuntoCorreo, mensajeCorreo));

                        getInData().getData().get(0).getData().remove(0);
                    } while (getInData().getData().get(0).getData().size() > 0);
                }
                sleep(250);
                
            }
            catch(Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
            }         
        }
    }
    
    private void ConectarAlMailServer() throws NoSuchProviderException, Exception
    {
        mail = new MailSender(propiedades.leer("CorreoElectronico"),propiedades.leer("CorreoUsuario"),cifrador.desencriptar(propiedades.leer("CorreoClave")),true,true,propiedades.leer("SMTPServer"),propiedades.leer("SMTPPort"),false);
    }
    
}
