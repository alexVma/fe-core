/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class XMLObjectGeneratorProcessor extends QueueProcessorThread
{
    private String facturaCustomVersion = "1.0.0";
    
    public XMLObjectGeneratorProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize, Propiedades pro) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        
        if(pro.get("facturaCustomVersion") != null)
        {
            facturaCustomVersion = (String)pro.get("facturaCustomVersion");
            System.out.println("facturaCustomVersion = " + facturaCustomVersion);
        }
        else
        {
            System.out.println("facturaCustomVersion == null, se usara la version(default): " + facturaCustomVersion);
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
                        
                        addTaskToQueue(new XMLObjectGeneratorTask(queueDataProcessor,this,facturaCustomVersion));

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
