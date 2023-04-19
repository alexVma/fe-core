/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class XMLSignerProcessor extends QueueProcessorThread
{
    private Propiedades propiedades = null;
    private String claveP12 = null;
    
    public XMLSignerProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize,Propiedades propiedades, String claveP12) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        this.propiedades = propiedades;
        this.claveP12 = claveP12;
    }

    @Override
    public void run() {
        while(true)
        {
            try 
            {
                if(getInData().getData().get(0).getData().size() > 0)
                {
                    do 
                    {
                        QueueDataProcessor queueDataProcessor = (QueueDataProcessor)getInData().getData().get(0).getData().get(0);
                        
                        addTaskToQueue(new XMLSignerTask(queueDataProcessor,propiedades,claveP12, this));

                        getInData().getData().get(0).getData().remove(0);
                        
                    }while (getInData().getData().get(0).getData().size() > 0);
                }
                sleep(100);
            }
            catch (Exception ex) 
            {
                Logger.getLogger(SriRecepcionProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
        
    }
    
}
