/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class WebServiceResponderProcessor  extends QueueProcessorThread
{

    public WebServiceResponderProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
    }

    @Override
    public void run() {
        
    }
    
}
