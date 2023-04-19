/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.cryptography.CifradorAESCBCPKCS5Padding;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class CheckAmbienteProcessor extends QueueProcessorThread 
{
    String ambienteAprobado = null;
    String rucAprobado = null;
    
    public CheckAmbienteProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize, Propiedades propiedades) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        
        ambienteAprobado = propiedades.leer("Ambiente");
        
        try
        {
            byte[] CLAVE_BYTES = "skei57todpa928fu".getBytes("UTF-8");
            byte[] SALT_BYTES =  "1l0s9djtuya347fydugv".getBytes("UTF-8");
            byte[] IV_BYTES = "dpwieuthv39567lp".getBytes("UTF-8");;
            CifradorAESCBCPKCS5Padding cifrador = new CifradorAESCBCPKCS5Padding(new String(CLAVE_BYTES),65536,128,IV_BYTES,SALT_BYTES);
            
            File directorioActual = new File(".");
            FilenameFilter filter = new FilenameFilter() 
            {
                public boolean accept(File directory, String fileName) 
                {
                    return fileName.endsWith(".aut");
                }
            };
            String resConf [] = directorioActual.list(filter);
            
            if(resConf != null)
            {
                if(resConf.length > 0)
                {
                    FileInputStream fis = new FileInputStream(resConf[0]);
                    byte disData [] = new byte[fis.available()];
                    fis.read(disData);
                    fis.close();
                    rucAprobado = cifrador.desencriptar(new String(disData, "UTF-8"));
                    System.out.println("Archivo: " + resConf[0] + " Se cargo exitosamente; Ruc autorizado: " + rucAprobado);
                }
                else
                {
                    System.out.println("No se puedo cargar el archivo aut: length = 0");
                }
            }
            else
            {
                System.out.println("No se puedo cargar el archivo aut: null");
            }
        }
        catch(Exception e)
        {
            rucAprobado = "";
            System.out.println("Error cargar el archivo aut");
            e.printStackTrace();
        }
        
    }

    @Override
    public void run() 
    {
       while (true) 
        {
            try
            {
                if(getInData().getData().get(0).getData().size() > 0)
                {
                    do 
                    {
                        QueueDataProcessor queueDataProcessor = (QueueDataProcessor)getInData().getData().get(0).getData().get(0);
                        
                        addTaskToQueue(new CheckAmbienteTask(queueDataProcessor,ambienteAprobado, rucAprobado, this));

                        getInData().getData().get(0).getData().remove(0);
                    } while (getInData().getData().get(0).getData().size() > 0);
                }
                sleep(100);
            }
            catch(Exception e)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
            }         
        }
    }
    
}
