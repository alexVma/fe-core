/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class WebServiceRequesterProcessor  extends QueueProcessorThread
{

    public WebServiceRequesterProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
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
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,"WebServiceRequesterProcessor: Se encontro " + getInData().getData().get(0).getData().size() + " documento(s) a procesar ");
                        
                        QueueDataProcessor queueDataProcessor = (QueueDataProcessor) (QueueDataProcessor)getInData().getData().get(0).getData().get(0);
                        //QueueDataProcessor queueDataProcessor = (QueueDataProcessor) getElement(0);
                        
                        //addTaskToQueue(new WebServiceRequesterTask(stringSigned, comprobanteElectronico,queueDataProcessor.getPdfByte(), mail));
                        
                        //getQueueThreadProcessorAfter().addElement(queueDataProcessor);
                        addDataToOutdata(queueDataProcessor);
                        
                        getInData().getData().get(0).getData().remove(0);
                        //removeElement(0);

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
    
}
