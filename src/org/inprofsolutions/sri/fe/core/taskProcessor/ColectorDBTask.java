/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.CampoAdicional;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DetalleAdicional;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.CreditoExtendido;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.DetalleCredito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.DetallesCredito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetalleFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetallesFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.impuestos.ImpuestoRetencion;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class ColectorDBTask implements Runnable
{
    String path = null;
    String ambienteAprobado = null;
    QueueProcessorThread queueThreadProcessor = null;
    String documentoArray [][] = null;
    
    public ColectorDBTask(String documentoArray [][], String path, String ambienteAprobando,QueueProcessorThread queueThreadProcessor) 
    {
        this.path = path;
        this.ambienteAprobado = ambienteAprobando;
        this.queueThreadProcessor = queueThreadProcessor;
        
        this.documentoArray = documentoArray;
    }
    
    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        try
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Documento encontrado: "+ org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(documentoArray[0][7]) +" - Iniciando proceso de documento en ambiente: " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla5.getLabel(documentoArray[0][0]));

            if(documentoArray[0][7].equals("01"))
            {
                Factura factura = new Factura();
                factura.setDocumentoVersion("1.0.0");
                
                factura.setCodigoAmbiente(documentoArray[0][1]);
                //factura.setCodigoAmbiente("1");
                
                factura.setCodigoTipoEmision(documentoArray[0][2]);
                factura.setRazonSocial(documentoArray[0][3]);
                factura.setNombreComercial(documentoArray[0][4]);
                factura.setRuc(documentoArray[0][5]);
                factura.setClaveAcceso(documentoArray[0][6]);
                factura.setCodigoDocumento(documentoArray[0][7]);
                factura.setEstablecimiento(documentoArray[0][8]);
                factura.setPuntoEmision(documentoArray[0][9]);
                factura.setSecuencial(documentoArray[0][10]);
                factura.setDireccionMatriz(documentoArray[0][11]);
                
                factura.setFechaEmision(documentoArray[0][12]);
                //factura.setFechaEmision("04/02/2015");
                
                factura.setDireccionEstablecimiento(documentoArray[0][13]);
                factura.setContribuyenteEspecial(documentoArray[0][14]);
                factura.setObligadoALlebarContabilidad(documentoArray[0][15]);
                factura.setTipoIdentificacionDelComprador(documentoArray[0][16]);
                factura.setGuiaRemision(documentoArray[0][17]);
                factura.setRazonSocialDelComprador(documentoArray[0][18]);
                factura.setIdentificacionDelComprador(documentoArray[0][19]);
                factura.setTotalSinImpuestos(documentoArray[0][20]);
                factura.setTotalDescuento(documentoArray[0][21]);

                if(!documentoArray[0][22].trim().equals(""))
                {
                    Impuesto impuesto = new Impuesto(
                                documentoArray[0][22], 
                                documentoArray[0][23],
                                null, 
                                documentoArray[0][24], 
                                documentoArray[0][25]);

                    factura.getTotalConImpuestos().getImpuesto().add(impuesto);
                }
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"documentoArray[0][22] = \"\"");
                }
                
                if(!documentoArray[0][26].trim().equals(""))
                {
                    Impuesto impuesto = new Impuesto(
                                documentoArray[0][26], 
                                documentoArray[0][27],
                                null, 
                                documentoArray[0][28], 
                                documentoArray[0][29]);

                    factura.getTotalConImpuestos().getImpuesto().add(impuesto);
                }
                else
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"documentoArray[0][29] = \"\"");
                }
                
                factura.setPropina(documentoArray[0][30]);
                factura.setImporteTotal(documentoArray[0][31]);
                factura.setMoneda(documentoArray[0][32]);
                for(int j = 0 ; j< documentoArray.length;j++)
                {
                    DetalleFactura detalleFactura = new DetalleFactura(documentoArray[j][33], documentoArray[j][34], documentoArray[j][35], documentoArray[j][36], documentoArray[j][37], documentoArray[j][38]);
                    if(documentoArray[j][39] != null)
                    {
                        if(documentoArray[j][39].trim().replaceAll(";", "").length() > 0)
                        {
                            String tmp = documentoArray[j][39].trim();

    //                        while (tmp.indexOf(";;") != -1);
    //                        {
    //                            tmp = tmp.replaceAll(";;", ";");
    //                            System.out.println("Despues de ;; = " + tmp);
    //                        }
    //                        
    //                        while (tmp.substring(0, 1).equals(";"));
    //                        {                            
    //                            tmp = tmp.substring(1, tmp.length());
    //                            System.out.println("Despues de ; antes = " + tmp);
    //                        } 
    //                        
    //                        while (tmp.substring(tmp.length()-1, tmp.length()).equals(";"));
    //                        {                            
    //                            tmp = tmp.substring(0, tmp.length()-1);
    //                            System.out.println("Despues de ; despues = " + tmp);
    //                        } 

                            detalleFactura.getDetallesAdicionales().getDetalleAdicional().add(new DetalleAdicional("Descripcion Adicional", tmp));
                        }
                    }
                    
                    if(!documentoArray[j][40].trim().equals(""))
                    {
                        Impuesto impuestoDetalle = new Impuesto(documentoArray[j][40], documentoArray[j][41],documentoArray[j][42], documentoArray[j][43], documentoArray[j][44]);
                        detalleFactura.getDetallesImpuestos().getDetalleImpuesto().add(impuestoDetalle);
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"documentoArray["+j+"][40] = \"\"");
                    }

                    if(!documentoArray[j][45].trim().equals(""))
                    {
                        Impuesto impuestoDetalle = new Impuesto(documentoArray[j][45], documentoArray[j][46],documentoArray[j][47], documentoArray[j][48], documentoArray[j][49]);
                        detalleFactura.getDetallesImpuestos().getDetalleImpuesto().add(impuestoDetalle);
                    }
                    else
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"documentoArray["+j+"][45] = \"\"");
                    }

                    DetallesFactura detallesFactura =  factura.getDetallesFactura();
                    detallesFactura.getDetalleFactura().add(detalleFactura);
                    factura.setDetallesFactura(detallesFactura);
                }

                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"documentoArray[0].length="+ documentoArray[0].length);

                if(documentoArray[0][50] != null)
                {
                    if(!documentoArray[0][50].trim().equals(""))
                    {
                        String adicionalArray [] = documentoArray[0][50].trim().split(";");
                        if(adicionalArray != null)
                        {
                            for(int k = 0; k < adicionalArray.length ; k++)
                            {
                                if(adicionalArray[k] != null)
                                {
                                    if(!adicionalArray[k].trim().equals(""))
                                    {
                                        if(adicionalArray[k].split(":").length == 2)
                                        {
                                            factura.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(adicionalArray[k].trim().split(":")[0], adicionalArray[k].trim().split(":")[1]));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
                queueDataProcessor.setDefaultComprobanteElectronico(factura);
                queueDataProcessor.setDbDocumentFilter(new String [][]{{"c_invoice_id",documentoArray[0][0]}});
                queueDataProcessor.setTipoIntegracion("DBSQL");
                        
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(documentoArray[0][7].equals("07"))
            {
                Retencion retencion = new Retencion();
                retencion.setDocumentoVersion("1.0.0");
                
                retencion.setCodigoAmbiente(documentoArray[0][1]);
                
                retencion.setCodigoTipoEmision(documentoArray[0][2]);
                retencion.setRazonSocial(documentoArray[0][3]);
                retencion.setNombreComercial(documentoArray[0][4]);
                retencion.setRuc(documentoArray[0][5]);
                retencion.setClaveAcceso(documentoArray[0][6]);
                retencion.setCodigoDocumento(documentoArray[0][7]);
                retencion.setEstablecimiento(documentoArray[0][8]);
                retencion.setPuntoEmision(documentoArray[0][9]);
                retencion.setSecuencial(documentoArray[0][10]);
                retencion.setDireccionMatriz(documentoArray[0][11]);
                
                retencion.setFechaEmision(documentoArray[0][12]);
                //retencion.setFechaEmision("01/01/2015");
                
                retencion.setDireccionEstablecimiento(documentoArray[0][13]);
                retencion.setContribuyenteEspecial(documentoArray[0][14]);
                retencion.setObligadoALlebarContabilidad(documentoArray[0][15]);
                retencion.setTipoIdentificacionSujetoRetenido(documentoArray[0][16]);
                retencion.setRazonSocialSujetoRetenido(documentoArray[0][17]);
                retencion.setIdentificacionSujetoRetenido(documentoArray[0][18]);
                retencion.setPeriodoFiscal(documentoArray[0][19]);

                for(int j = 0 ; j< documentoArray.length;j++)
                {
                    ImpuestoRetencion impuestoRetencion = new ImpuestoRetencion(documentoArray[j][20], documentoArray[j][21], documentoArray[j][22], documentoArray[j][23], documentoArray[j][24], "0"+documentoArray[j][25],documentoArray[j][26], documentoArray[j][27]);

                    retencion.getImpuestosRetencion().getImpuestoRetencion().add(impuestoRetencion);
                }

                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"documentoArray[0].length="+ documentoArray[0].length);

                //retencion.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(documentoArray[0][28].trim().split(":")[0], documentoArray[0][28].trim().split(":")[1]));
                
                if(documentoArray[0][28] != null)
                {
                    if(!documentoArray[0][28].trim().equals(""))
                    {
                        String adicionalArray [] = documentoArray[0][28].trim().split(";");
                        if(adicionalArray != null)
                        {
                            for(int k = 0; k < adicionalArray.length ; k++)
                            {
                                if(adicionalArray[k] != null)
                                {
                                    if(!adicionalArray[k].trim().equals(""))
                                    {
                                        if(adicionalArray[k].split(":").length == 2)
                                        {
                                            retencion.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(adicionalArray[k].trim().split(":")[0], adicionalArray[k].trim().split(":")[1]));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
                queueDataProcessor.setDefaultComprobanteElectronico(retencion);
                queueDataProcessor.setDbDocumentFilter(new String [][]{{"co_retencion_compra.co_retencion_compra_id",documentoArray[0][0]}});
                queueDataProcessor.setTipoIntegracion("DBSQL");
                
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(documentoArray[0][7].equals("04"))
            {
                CreditoExtendido credito = new CreditoExtendido();
                credito.setDocumentoVersion("1.0.0");
                
                credito.setCodigoAmbiente(documentoArray[0][1]);
                
                credito.setCodigoTipoEmision(documentoArray[0][2]);
                credito.setRazonSocial(documentoArray[0][3]);
                credito.setNombreComercial(documentoArray[0][4]);
                credito.setRuc(documentoArray[0][5]);
                credito.setClaveAcceso(documentoArray[0][6]);
                credito.setCodigoDocumento(documentoArray[0][7]);
                credito.setEstablecimiento(documentoArray[0][8]);
                credito.setPuntoEmision(documentoArray[0][9]);
                credito.setSecuencial(documentoArray[0][10]);
                credito.setDireccionMatriz(documentoArray[0][11]);
                
                credito.setFechaEmision(documentoArray[0][12]);
                //credito.setFechaEmision("12/02/2015");
                
                credito.setDireccionEstablecimiento(documentoArray[0][13]);
                credito.setTipoIdentificacionDelComprador(documentoArray[0][14]);
                credito.setRazonSocialDelComprador(documentoArray[0][15]);
                credito.setIdentificacionDelComprador(documentoArray[0][16]);
                credito.setContribuyenteEspecial(documentoArray[0][17]);
                credito.setObligadoALlebarContabilidad(documentoArray[0][18]);
                credito.setRise(documentoArray[0][19]);
                credito.setCodDocModificado(documentoArray[0][20]);
                credito.setNumDocModificado(documentoArray[0][21]);
                credito.setFechaEmisionDocSustento(documentoArray[0][22]);
                credito.setTotalSinImpuestos(documentoArray[0][23]);

                credito.setValorModificacion(documentoArray[0][24]);

                credito.setMoneda(documentoArray[0][25]);

                for(int j = 0 ; j< documentoArray.length;j++)
                {
                    DetalleCredito detalleCredito = new DetalleCredito(documentoArray[j][27], documentoArray[j][28], documentoArray[j][29], documentoArray[j][30], documentoArray[j][31], documentoArray[j][32]);
                    
                    if(!documentoArray[j][33].trim().equals(""))
                    {
                        Impuesto impuestoDetalle = new Impuesto(documentoArray[j][33], documentoArray[j][34],documentoArray[j][35], documentoArray[j][36], documentoArray[j][37]);
                        detalleCredito.getImpuestosDetalle().getImpuestoDetalle().add(impuestoDetalle);
                    }
                    
                    if(!documentoArray[j][38].trim().equals(""))
                    {
                        Impuesto impuestoDetalle = new Impuesto(documentoArray[j][38], documentoArray[j][39],documentoArray[j][40], documentoArray[j][41], documentoArray[j][42]);
                        detalleCredito.getImpuestosDetalle().getImpuestoDetalle().add(impuestoDetalle);
                    }
                    
                    DetallesCredito detallesCredito =  credito.getDetallesCredito();
                    detallesCredito.getDetalleCredito().add(detalleCredito);
                    credito.setDetallesCredito(detallesCredito);
                }

                credito.setMotivo(documentoArray[0][26]);

                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"documentoArray[0].length="+ documentoArray[0].length);

                //credito.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(documentoArray[0][43].trim().split(":")[0], documentoArray[0][43].trim().split(":")[1]));
                
                if(documentoArray[0][43] != null)
                {
                    if(!documentoArray[0][43].trim().equals(""))
                    {
                        String adicionalArray [] = documentoArray[0][43].trim().split(";");
                        if(adicionalArray != null)
                        {
                            for(int k = 0; k < adicionalArray.length ; k++)
                            {
                                if(adicionalArray[k] != null)
                                {
                                    if(!adicionalArray[k].trim().equals(""))
                                    {
                                        if(adicionalArray[k].split(":").length == 2)
                                        {
                                            credito.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(adicionalArray[k].trim().split(":")[0], adicionalArray[k].trim().split(":")[1]));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
                queueDataProcessor.setDefaultComprobanteElectronico(credito);
                queueDataProcessor.setDbDocumentFilter(new String [][]{{"c_invoice_id",documentoArray[0][0]}});
                queueDataProcessor.setTipoIntegracion("DBSQL");
                
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Error - Tipo de documento invalido: " + documentoArray[0][6] + " - " + "El documento no se agrega a la siguiente cola");
            }
            
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }   
}