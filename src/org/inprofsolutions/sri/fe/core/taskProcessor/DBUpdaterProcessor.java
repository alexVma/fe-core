/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.dataBase.DataBaseConnectionWizard;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class DBUpdaterProcessor extends QueueProcessorThread 
{
    DataBaseConnectionWizard db = null;
    
    public DBUpdaterProcessor(int poolSize, int maxPoolSize, long keepAliveTime, int ArrayQueueSize,DataBaseConnectionWizard db) {
        super(poolSize, maxPoolSize, keepAliveTime, ArrayQueueSize);
        
        this.db = db;
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
                        
                        String estadoResutado = null;
                        switch (queueDataProcessor.getEstadoDocumento()) 
                        {
                            case "DEVUELTA":
                                estadoResutado = "R";
                                break;
                            case "NO AUTORIZADO":
                                estadoResutado = "R";
                                break;
                            case "AUTORIZADO":
                                estadoResutado = "P";
                                break;
                            default: 
                                estadoResutado = "D";
                                break;
                        }
                        
                        DefaultComprobanteElectronico comprobanteElectronico = queueDataProcessor.getDefaultComprobanteElectronico();
                        
                        String NumeroDeAutorizacion = "";
                        String claveAcceso = "";
                        String fecha = "";
                        if(queueDataProcessor.getEstadoDocumento().equals("AUTORIZADO"))
                        {
                            NumeroDeAutorizacion = queueDataProcessor.getRespuestaComprobante().getAutorizaciones().getAutorizacion().get(0).getNumeroAutorizacion();
                            fecha = queueDataProcessor.getRespuestaComprobante().getAutorizaciones().getAutorizacion().get(0).getFechaAutorizacion().toString();
                            claveAcceso = comprobanteElectronico.getClaveAcceso();
                        }
                        
                        if(comprobanteElectronico.getCodigoDocumento().equals("01") || comprobanteElectronico.getCodigoDocumento().equals("04"))
                        {
                            String sql = "update c_invoice SET em_indfe_estado = '"+estadoResutado+"',em_co_nro_aut_sri = '"+NumeroDeAutorizacion+"', em_indfe_clave_acceso = '"+claveAcceso+"', em_indfe_fecha_respuesta = '"+fecha+"' where "+queueDataProcessor.getDbDocumentFilter()[0][0]+" like '"+queueDataProcessor.getDbDocumentFilter()[0][1]+"';";
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,sql);                        
                            db.sqlVoid(sql);
                        }
                        else if(comprobanteElectronico.getCodigoDocumento().equals("07"))
                        {
                            String sql = "update co_retencion_compra SET em_indfe_estado = '"+estadoResutado+"' where "+queueDataProcessor.getDbDocumentFilter()[0][0]+" like '"+queueDataProcessor.getDbDocumentFilter()[0][1]+"';";
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,sql);                        
                            db.sqlVoid(sql);
                            
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,"No se dispone del SQL para la actualizacion de la retencion origen.");
                        }

                        
                        String Comprador = null;
                        if(
                                comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.FacturaExtendida") || 
                                comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura")
                                )
                        {
                            Comprador = ((Factura)comprobanteElectronico).getIdentificacionDelComprador();
                        }
                        else if(
                                comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.CreditoExtendido") || 
                                comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito"))
                        {
                            Comprador = ((Credito)comprobanteElectronico).getIdentificacionDelComprador();
                        }
                        else if(
                                comprobanteElectronico.getClass().getName().equals("org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion"))
                        {
                            Comprador = ((Retencion)comprobanteElectronico).getIdentificacionSujetoRetenido();
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Comprador = null, No se pudo determinar el tipo de documento");
                        }
                        
                        String sqlResultado = "insert into indfe_resultado \n" +
                                                "select \n" +
                                                "get_uuid(), ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby \n" +
                                                ",'"+comprobanteElectronico.getCodigoDocumento()+"','"+comprobanteElectronico.getEstablecimiento()+"','"+comprobanteElectronico.getPuntoEmision()+"','"+comprobanteElectronico.getSecuencial()+"','"+fecha+"','"+comprobanteElectronico.getClaveAcceso()+"','"+NumeroDeAutorizacion+"','"+queueDataProcessor.getEstadoDocumento()+"','"+comprobanteElectronico.getCodigoAmbiente()+"','"+queueDataProcessor.getDbDocumentFilter()[0][1]+"','"+ Comprador +"' \n" +
                                                "from indfe_vis_facturas_v where id_factura='"+queueDataProcessor.getDbDocumentFilter()[0][1]+"'";
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,sqlResultado); 
                        db.sqlVoid(sqlResultado);
                        
                        if(queueDataProcessor.getEstadoDocumento().equals("DEVUELTA") || queueDataProcessor.getEstadoDocumento().equals("NO AUTORIZADO"))
                        {
                            String sqlGetResultadoPK = "SELECT \n" +
                                "indfe_resultado_id \n" +
                                "FROM indfe_resultado where c_invoice_id like '"+queueDataProcessor.getDbDocumentFilter()[0][1]+"' and clave_acceso like '"+comprobanteElectronico.getClaveAcceso()+"'";
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,sqlGetResultadoPK); 
                            String resultadoPK [][] = db.sqlArray(sqlGetResultadoPK);
                            
                            if(queueDataProcessor.getEstadoDocumento().equals("DEVUELTA")) 
                            {
                                sri.gob.ec.client.ws.recepcion.Comprobante.Mensajes m = queueDataProcessor.getRespuestaSolicitud().getComprobantes().getComprobante().get(0).getMensajes();

                                for(int k = 0 ; k < m.getMensaje().size() ; k++)
                                {
                                    String informacionAdicional = "";
                                    if(m.getMensaje().get(k).getInformacionAdicional() != null)
                                    {
                                        informacionAdicional = m.getMensaje().get(k).getInformacionAdicional().replaceAll("'", "");
                                    }
                                    
                                    String mensaje = "";
                                    if(m.getMensaje().get(k).getMensaje() != null)
                                    {
                                        mensaje = m.getMensaje().get(k).getMensaje().replaceAll("'", "");
                                    }
                                    
                                    String sqlMesaje = "insert into indfe_mensaje \n" +
                                    "select \n" +
                                    "get_uuid(), ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, \n" +
                                    "'"+resultadoPK[0][0]+"','"+informacionAdicional+"','"+mensaje+"','"+m.getMensaje().get(k).getTipo()+"','"+m.getMensaje().get(k).getIdentificador()+"' \n" +
                                    "from indfe_vis_facturas_v where id_factura='"+queueDataProcessor.getDbDocumentFilter()[0][1]+"'";
                                    LoggerContainer.getListLogger().get(0).log(Level.INFO,sqlMesaje); 
                                    db.sqlVoid(sqlMesaje);
                                }

                            }
                            else if(queueDataProcessor.getEstadoDocumento().equals("NO AUTORIZADO"))
                            {
                                sri.gob.ec.client.ws.autorizacion.Autorizacion.Mensajes m = queueDataProcessor.getRespuestaComprobante().getAutorizaciones().getAutorizacion().get(0).getMensajes();

                                for(int k = 0 ; k < m.getMensaje().size() ; k++)
                                {
                                    String informacionAdicional = "";
                                    if(m.getMensaje().get(k).getInformacionAdicional() != null)
                                    {
                                        informacionAdicional = m.getMensaje().get(k).getInformacionAdicional().replaceAll("'", "");
                                    }
                                    
                                    String mensaje = "";
                                    if(m.getMensaje().get(k).getMensaje() != null)
                                    {
                                        mensaje = m.getMensaje().get(k).getMensaje().replaceAll("'", "");
                                    }
                                    
                                    String sqlMesaje = "insert into indfe_mensaje \n" +
                                    "select \n" +
                                    "get_uuid(), ad_client_id, ad_org_id, isactive, created, createdby, updated, updatedby, \n" +
                                    "'"+resultadoPK[0][0]+"','"+informacionAdicional+"','"+mensaje+"','"+m.getMensaje().get(k).getTipo()+"','"+m.getMensaje().get(k).getIdentificador()+"' \n" +
                                    "from indfe_vis_facturas_v where id_factura='"+queueDataProcessor.getDbDocumentFilter()[0][1]+"'";
                                    LoggerContainer.getListLogger().get(0).log(Level.INFO,sqlMesaje); 
                                    db.sqlVoid(sqlMesaje);
                                }
                            }
                        }
                        getInData().getData().get(0).getData().remove(0);
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