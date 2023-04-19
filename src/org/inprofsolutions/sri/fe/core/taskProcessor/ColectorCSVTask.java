/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.File;
import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.Carpetas;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.ParserCSV;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class ColectorCSVTask implements Runnable
{
    File fileCabecera = null;
    File fileDetalle = null;
    String path = null;
    String ambienteAprobado = null;
    QueueProcessorThread queueThreadProcessor = null;
    
    public ColectorCSVTask(File fileCabecera,File fileDetalle, String path,QueueProcessorThread queueThreadProcessor) 
    {
        this.fileCabecera = fileCabecera;
        this.fileDetalle = fileDetalle;
        this.path = path;
        this.queueThreadProcessor = queueThreadProcessor;
    }
    
    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        
        try
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Procesando documento: " + fileCabecera.getName().substring(9).replaceFirst(".csv", ""));
            String cabeceraCSV [][] = null;
            String detalleCSV [][] = null;

            cabeceraCSV  = ParserCSV.parserCSV(new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+fileCabecera.getName()),taskID);
            detalleCSV  = ParserCSV.parserCSV(new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+fileDetalle.getName()),taskID);
            
            QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
            queueDataProcessor.setFileCabecera(fileCabecera);
            queueDataProcessor.setFileDetalle(fileDetalle);
            queueDataProcessor.setCabeceraArray(cabeceraCSV);
            queueDataProcessor.setDetalleArray(detalleCSV);
            queueDataProcessor.setTipoIntegracion("CSV");
            
            queueThreadProcessor.addDataToOutdata(queueDataProcessor);
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }
}