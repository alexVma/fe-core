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
public class XMLW3CGeneratorProcessor extends QueueProcessorThread
{
    private Propiedades propiedades = null;
    
    public XMLW3CGeneratorProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize,Propiedades propiedades) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        this.propiedades = propiedades;
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
                        
                        addTaskToQueue(new XMLW3CGeneratorTask(queueDataProcessor,propiedades, this));

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
