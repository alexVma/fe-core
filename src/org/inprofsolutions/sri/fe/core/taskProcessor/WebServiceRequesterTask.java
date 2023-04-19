/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;

/**
 *
 * @author caf
 */
public class WebServiceRequesterTask  implements Runnable
{

    @Override
    public void run() 
    {
//        try
//        {
//            Container.getListLogger().get(0).log(Level.INFO,"procesando documento: " + fileCabecera.getName().substring(9).replaceFirst(".csv", ""));
//            Container.getListLogger().get(0).log(Level.INFO,"Razon Social: " + cabeceraCSV[1][2]);
//
//            if(tipoDeAmbienteAprobado(cabeceraCSV[1][0]))
//            {
//                if(cabeceraCSV[1][6].equals("01"))
//                {
//                    Container.getListLogger().get(0).log(Level.INFO,"Documento encontrado: FACTURA - Iniciando proceso de documento");
//
//                    QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
//                    queueDataProcessor.setDefaultComprobanteElectronico(factura);
//                    queueThreadProcessor.getQueueThreadProcessorAfter().addElement(queueDataProcessor);
//                    
////                    Document doc = generador.getFacturaXML(factura);
////                    firmarYmandar(doc, factura, fileCabecera, fileDetalle);
//                }
//                else if(cabeceraCSV[1][6].equals("04"))
//                {
//                    Container.getListLogger().get(0).log(Level.INFO,"Documento encontrado: NOTA DE CREDITO - Iniciando proceso de documento");
//
//                    QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
//                    queueDataProcessor.setDefaultComprobanteElectronico(credito);
//                    queueThreadProcessor.getQueueThreadProcessorAfter().addElement(queueDataProcessor);
//
////                    Document doc = generador.getCreditoXML(credito);
////                    firmarYmandar(doc, credito, fileCabecera, fileDetalle);
//                }
//                else if(cabeceraCSV[1][6].equals("05"))
//                {
//                    Container.getListLogger().get(0).log(Level.INFO,"Documento encontrado: NOTA DE DEBITO - Iniciando proceso de documento");
//
//                    QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
//                    queueDataProcessor.setDefaultComprobanteElectronico(debito);
//                    queueThreadProcessor.getQueueThreadProcessorAfter().addElement(queueDataProcessor);
//                    
////                    Document doc = generador.getDebitoXML(debito);
////                    firmarYmandar(doc, debito, fileCabecera, fileDetalle);
//                }
//                else if(cabeceraCSV[1][6].equals("06"))
//                {
//                    Container.getListLogger().get(0).log(Level.INFO,"Documento encontrado: GUIA DE REMISION - Iniciando proceso de documento");
//
//                    QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
//                    queueDataProcessor.setDefaultComprobanteElectronico(remision);
//                    queueThreadProcessor.getQueueThreadProcessorAfter().addElement(queueDataProcessor);
//                    
////                    Document doc = generador.getRemisionXML(remision);
////                    firmarYmandar(doc, remision, fileCabecera, fileDetalle);
//                }
//                else if(cabeceraCSV[1][6].equals("07"))
//                {
//                    Container.getListLogger().get(0).log(Level.INFO,"Documento encontrado: RETENCION - Iniciando proceso de documento");
//
//                    QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
//                    queueDataProcessor.setDefaultComprobanteElectronico(retencion);
//                    queueThreadProcessor.getQueueThreadProcessorAfter().addElement(queueDataProcessor);
//                    
////                    Document doc = generador.getRetencionXML(retencion);
////                    firmarYmandar(doc, retencion, fileCabecera, fileDetalle);
//                }
//                else
//                {
//                    Container.getListLogger().get(0).log(Level.INFO,"Error - Tipo de documento invalido: " + cabeceraCSV[1][6] + " - " + "El documento no se agrega a la siguiente cola");
//                }
//            }
//            else
//            {
//                Container.getListLogger().get(0).log(Level.INFO,"El Ambiente " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla5.getLabel(cabeceraCSV[1][0]) + " no esta habilidado");
//            }
//        }
//        catch(Exception e)
//        {
//            Container.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
//        }
    }
    
}
