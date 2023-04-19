/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class CheckAmbienteTask implements Runnable 
{
    QueueProcessorThread queueThreadProcessor = null;
    QueueDataProcessor queueDataProcessor = null;
    String ambienteAprobado = null;
    String rucAprobando = null;
    
    public CheckAmbienteTask(QueueDataProcessor queueDataProcessor, String ambienteAprobando, String rucAprobando, QueueProcessorThread queueThreadProcessor) 
    {
        this.queueThreadProcessor = queueThreadProcessor;
        this.queueDataProcessor = queueDataProcessor;
        
        this.ambienteAprobado = ambienteAprobando;
        this.rucAprobando = rucAprobando;
    }
    
    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        
        try
        {
            if(tipoDeAmbienteAprobado(queueDataProcessor.getDefaultComprobanteElectronico().getCodigoAmbiente(),taskID))
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"El Ambiente de " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla5.getLabel(queueDataProcessor.getDefaultComprobanteElectronico().getCodigoAmbiente()) + " esta habilitado");
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"El Ambiente de " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla5.getLabel(queueDataProcessor.getDefaultComprobanteElectronico().getCodigoAmbiente()) + " no esta habilidado");
            }
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }
    
    private boolean tipoDeAmbienteAprobado(String ambienteDocumento, String TaskID)
    {
        if(ambienteAprobado.equals("Pruebas"))
        {
            LoggerContainer.getListLogger().get(0).log(Level.FINEST,TaskID + " - " +"Ambiente habilitado: Pruebas");
            if(ambienteDocumento.equals("1"))
            {
                LoggerContainer.getListLogger().get(0).log(Level.FINEST,TaskID + " - " +"Se autoriza el proceso del documento en ambiente de Pruebas: true");
                return true;
            }
        }
        else if(ambienteAprobado.equals("Produccion y Pruebas"))
        {
            LoggerContainer.getListLogger().get(0).log(Level.FINEST,TaskID + " - " +"Ambiente habilitado: Produccion y Pruebas");
            if(ambienteDocumento.equals("1"))
            {
                LoggerContainer.getListLogger().get(0).log(Level.FINEST,TaskID + " - " +"Se autoriza el proceso del documento en ambiente de Pruebas");
                return true;
            }
            else if(ambienteDocumento.equals("2")) //comentar
            {//alex v: documento todo esto para q permita cualquier ruc y no este comprobando con el archivo .aut
              //  if(queueDataProcessor.getDefaultComprobanteElectronico().getRuc().equals(rucAprobando))
               // {
                    LoggerContainer.getListLogger().get(0).log(Level.FINEST,TaskID + " - " +"Se autoriza el proceso del documento en ambiente de Produccion");
                    return true;
               /* }
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,TaskID + " - " +"El ruc no se encuentra habilitado en ambiente de Produccion");
                    return false;
                }*/
                
            }
        }
        LoggerContainer.getListLogger().get(0).log(Level.INFO,TaskID + " - " +"El ambiente solicitado no esta habilitado en Factura Lista, No se autoriza el proceso del documento: return false");
        return false;
    }
}
