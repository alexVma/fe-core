/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.File;
import java.io.FilenameFilter;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.Carpetas;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class ColectorCSVProcessor extends QueueProcessorThread
{
    File csvpendientes = null;
    FilenameFilter filterCabecera = null;
    String path = null;
    
    public ColectorCSVProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize, Propiedades propiedades) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        
        path = propiedades.leer("PathDirectorio");
        
        csvpendientes = new File(path+File.separator+Carpetas.ficheroscsvpendientes);
        filterCabecera = new FilenameFilter() 
        {
            public boolean accept(File directory, String fileName) 
            {
                return fileName.startsWith("cabecera");
            }
        };
    }

    @Override
    public void run() 
    {
        while(true)
        {
            try 
            {
                String resConf [] = csvpendientes.list(filterCabecera);
                for(int i = 0 ; i < resConf.length ; i++)
                {
                    File fileCabecera = new File(csvpendientes.getPath() +File.separator+resConf[i]);
                    File fileDetalle = new File(csvpendientes.getPath() +File.separator+"detalle"+resConf[i].substring(8));
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"Se encontro la cabecera: " + fileCabecera.getAbsolutePath());
                    boolean detalle = fileDetalle.exists();
                    if(detalle)
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,"Se encontro el detalle: " + fileDetalle.getAbsolutePath());
                        boolean checkfiles = checkFilesClosed(new File []{fileCabecera,fileDetalle});
                        if(checkfiles)
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.FINEST,"los archivos estan cerrados");
                            boolean procesando = fileMove(new File []{fileCabecera,fileDetalle}, path+File.separator+Carpetas.ficheroscsvprocesando);
                            if(procesando)
                            {
                                addTaskToQueue(new ColectorCSVTask(fileCabecera, fileDetalle, path, this));
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,"No se pudo mover los archivos: ERROR...");
                            }
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.FINEST,"Los archivos estan abiertos");
                        }
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,"No Se encontro el detalle: " + fileDetalle.getAbsolutePath());
                    }
                }
                sleep(700);
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(ColectorCSVProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean checkFilesClosed(File [] archivos)
    {
        try
        {
            for(int i = 0; i < archivos.length;i++)
            {
                if(archivos[i].renameTo(archivos[i])){
                    // if the file is renamed
                    //Container.getListLogger().get(0).log(Level.INFO,"file is closed");    
                }else{
                    // if the file didnt accept the renaming operation
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"El archivo esta abrierto:" + archivos[i].getName());
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
        return true;
    }
    
    public boolean fileMove(File [] archivos,String newPath)
    {
        try
        {
            for(int i = 0; i < archivos.length;i++)
            {             
                if(archivos[i].renameTo(new File(newPath+File.separator + archivos[i].getName())))
                {
                     LoggerContainer.getListLogger().get(0).log(Level.INFO,"Se movio el archivo: " + newPath+File.separator + archivos[i].getName());
                }
                else
                {
                     LoggerContainer.getListLogger().get(0).log(Level.INFO,"No se puede mover el archivo: " + archivos[i].getName() + " al directorio: " + newPath);
                     return false;
                }
            }
    	}
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
    	}
        return true;
    }
}
