/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.dataBase.DataBaseConnectionWizard;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class ColectorDBProcessor extends QueueProcessorThread
{
    String path = null;
    String ambienteAprobado = null;
    DataBaseConnectionWizard db = null;
    
    public ColectorDBProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize, Propiedades propiedades,DataBaseConnectionWizard db) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        
        path = propiedades.leer("PathDirectorio");
        ambienteAprobado = propiedades.leer("Ambiente");
        
        this.db = db;
    }

    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        
        while(true)
        {
            try 
            {
                //String resultado [] [] = db.sqlArray("select estab,ptoemi,secuencial,id_factura from indfe_rep_facturas_v where id_factura like 'EAA7DA02F653426D94A900928CA2771A' group by estab,ptoemi,secuencial,id_factura limit 1");
                String resultado [] [] = db.sqlArray("select estab,ptoemi,secuencial,id_factura from indfe_rep_facturas_v group by estab,ptoemi,secuencial,id_factura limit 5");
                if(resultado != null)
                {
                    for(int i = 0 ; i < resultado.length ; i++)
                    {
                        String [][] documento = db.sqlArray("select * from indfe_rep_facturas_v where id_factura like '"+resultado[i][3]+"';");
                        db.sqlVoid("update c_invoice SET em_indfe_estado = 'T' where c_invoice_id like '"+resultado[i][3]+"';");
                        addTaskToQueue(new ColectorDBTask(documento, path, ambienteAprobado, this));
                    }
                }
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"No se ha encontrado facturas para procesar");
                }

                
                String resultadoCredito [] [] = db.sqlArray("select estab,ptoemi,secuencial,id_notacredito from indfe_rep_notascred_v group by estab,ptoemi,secuencial,id_notacredito limit 5");
                if(resultadoCredito != null)
                {               
                    for(int i = 0 ; i < resultadoCredito.length ; i++)
                    {
                        String [][] documento = db.sqlArray("select * from indfe_rep_notascred_v where id_notacredito like '"+resultadoCredito[i][3]+"';");
                        db.sqlVoid("update c_invoice SET em_indfe_estado = 'T' where c_invoice_id like '"+resultadoCredito[i][3]+"';");
                        addTaskToQueue(new ColectorDBTask(documento, path, ambienteAprobado, this));
                    }
                }
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"No se ha encontrado Notas de Credito para procesar");
                }   

                
//                String resultadoRetencion [] [] = db.sqlArray("select estab::text,ptoemi::text,secuencial,retencion_id from indfe_rep_retencion_v group by estab::text,ptoemi::text,secuencial,retencion_id limit 1;");
//                if(resultadoRetencion != null)
//                {    
//                    for(int i = 0 ; i < resultadoRetencion.length ; i++)
//                    {
//                        String [][] documento = db.sqlArray("select * from indfe_rep_retencion_v where retencion_id like '"+resultadoRetencion[i][3]+"';" );
//                        db.sqlVoid("update co_retencion_compra SET em_indfe_estado = 'T' where co_retencion_compra.co_retencion_compra_id like '"+resultadoRetencion[i][3]+"';");
//                        addTaskToQueue(new ColectorDBTask(documento, path, ambienteAprobado, this));
//                    }
//                }
//                else
//                {
//                    System.out.println("Nada que procesar Retencion");
//                }   
                
                if(developer.isDebug())
                {
                    //sleep(600000); //10 minutos Desarrollo
                    sleep(15000); //15 segundos Desarrollo
                }
                else
                {
                    sleep(30000); //30 seguntos Normal
                }
            }
            catch(Exception e)
            {
                Logger.getLogger(ColectorDBProcessor.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
