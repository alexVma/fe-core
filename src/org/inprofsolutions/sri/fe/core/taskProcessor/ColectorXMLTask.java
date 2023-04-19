/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.io.File;
import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.CampoAdicional;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DetalleAdicional;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.CreditoExtendido;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.DetalleCredito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.DetallesCredito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.DebitoExtendido;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos.MotivoDebito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos.MotivosDebito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.FacturaExtendida;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetalleFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetallesFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.DestinatarioRemision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.detalles.DetalleRemision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.impuestos.ImpuestoRetencion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.xml.XMLDocumentLoad;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.Carpetas;
import org.inprofsolutions.sri.fe.core.integrator.baseCSV.ParserCSV;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author caf
 */
public class ColectorXMLTask implements Runnable
{
    File fileCabecera = null;
    String path = null;
    String ambienteAprobado = null;
    QueueProcessorThread queueThreadProcessor = null;
    
    public ColectorXMLTask(File fileCabecera, String path, String ambienteAprobando,QueueProcessorThread queueThreadProcessor) 
    {
        this.fileCabecera = fileCabecera;
        this.path = path;
        this.ambienteAprobado = ambienteAprobando;
        this.queueThreadProcessor = queueThreadProcessor;
    }
    
    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        
        try
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Procesando documento: " + fileCabecera.getName().substring(9).replaceFirst(".csv", ""));

            XMLDocumentLoad xMLDocumentLoad = new XMLDocumentLoad();
            Document doc = xMLDocumentLoad.getGenerateDocument(new File(path+File.separator+Carpetas.ficheroscsvprocesando+File.separator+fileCabecera.getName()));
            
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Documento encontrado: "+ org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(doc.getElementsByTagName("codDoc").item(0).getTextContent()) +" - Iniciando proceso de documento en ambiente: " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla5.getLabel(doc.getElementsByTagName("ambiente").item(0).getTextContent()));

            if(doc.getElementsByTagName("codDoc").item(0).getTextContent().equals("01"))
            {
                Factura factura = new Factura();            
                factura.setDocumentoVersion("1.1.0");
                factura.setCodigoAmbiente(doc.getElementsByTagName("ambiente").item(0).getTextContent());
                factura.setCodigoTipoEmision(doc.getElementsByTagName("tipoEmision").item(0).getTextContent());
                factura.setRazonSocial(doc.getElementsByTagName("razonSocial").item(0).getTextContent());
                factura.setNombreComercial(doc.getElementsByTagName("nombreComercial").item(0).getTextContent());
                factura.setRuc(doc.getElementsByTagName("ruc").item(0).getTextContent());
                factura.setClaveAcceso(doc.getElementsByTagName("claveAcceso").item(0).getTextContent());
                factura.setCodigoDocumento(doc.getElementsByTagName("codDoc").item(0).getTextContent());
                factura.setEstablecimiento(doc.getElementsByTagName("estab").item(0).getTextContent());
                factura.setPuntoEmision(doc.getElementsByTagName("ptoEmi").item(0).getTextContent());
                factura.setSecuencial(doc.getElementsByTagName("secuencial").item(0).getTextContent());
                factura.setDireccionMatriz(doc.getElementsByTagName("dirMatriz").item(0).getTextContent());
                factura.setFechaEmision(doc.getElementsByTagName("fechaEmision").item(0).getTextContent());
                factura.setDireccionEstablecimiento(doc.getElementsByTagName("dirEstablecimiento").item(0).getTextContent());
                if(doc.getElementsByTagName("contribuyenteEspecial").item(0) != null)
                {
                    factura.setContribuyenteEspecial(doc.getElementsByTagName("contribuyenteEspecial").item(0).getTextContent());
                }
                factura.setObligadoALlebarContabilidad(doc.getElementsByTagName("obligadoContabilidad").item(0).getTextContent());
                factura.setTipoIdentificacionDelComprador(doc.getElementsByTagName("tipoIdentificacionComprador").item(0).getTextContent());
                //factura.setGuiaRemision(doc.getElementsByTagName("ambiente").item(0).getTextContent());
                factura.setRazonSocialDelComprador(doc.getElementsByTagName("razonSocialComprador").item(0).getTextContent());
                factura.setIdentificacionDelComprador(doc.getElementsByTagName("identificacionComprador").item(0).getTextContent());
                factura.setTotalSinImpuestos(doc.getElementsByTagName("totalSinImpuestos").item(0).getTextContent());
                factura.setTotalDescuento(doc.getElementsByTagName("totalDescuento").item(0).getTextContent());

                Element totalConImpuestosContenedor = (Element)doc.getElementsByTagName("totalConImpuestos").item(0);
                NodeList totalImpuestosList = totalConImpuestosContenedor.getElementsByTagName("totalImpuesto");
                for(int k = 0 ; k< totalImpuestosList.getLength();k++)
                {
                    Element totalImpuesto = (Element)totalImpuestosList.item(k);
                    Impuesto impuesto = new Impuesto(
                            totalImpuesto.getElementsByTagName("codigo").item(0).getTextContent(), 
                            totalImpuesto.getElementsByTagName("codigoPorcentaje").item(0).getTextContent(),
                            null, 
                            totalImpuesto.getElementsByTagName("baseImponible").item(0).getTextContent(), 
                            totalImpuesto.getElementsByTagName("valor").item(0).getTextContent());
                    factura.getTotalConImpuestos().getImpuesto().add(impuesto);
                }

                factura.setPropina(doc.getElementsByTagName("propina").item(0).getTextContent());
                factura.setImporteTotal(doc.getElementsByTagName("importeTotal").item(0).getTextContent());
                factura.setMoneda(doc.getElementsByTagName("moneda").item(0).getTextContent());

                Element detallesContenedor = (Element)doc.getElementsByTagName("detalles").item(0);
                NodeList detallesList = detallesContenedor.getElementsByTagName("detalle");

                for(int j = 0 ; j< detallesList.getLength();j++)
                {
                    Element detalle = (Element)detallesList.item(j);
                    DetalleFactura detalleFactura = new DetalleFactura(
                            detalle.getElementsByTagName("codigoPrincipal").item(0).getTextContent(), 
                            detalle.getElementsByTagName("descripcion").item(0).getTextContent(), 
                            detalle.getElementsByTagName("cantidad").item(0).getTextContent(), 
                            detalle.getElementsByTagName("precioUnitario").item(0).getTextContent(), 
                            detalle.getElementsByTagName("descuento").item(0).getTextContent(), 
                            detalle.getElementsByTagName("precioTotalSinImpuesto").item(0).getTextContent());

                    if(doc.getElementsByTagName("detallesAdicionales") != null)
                    {
                        Element infoAdicionalContenedor = (Element)doc.getElementsByTagName("detallesAdicionales").item(0);
                        if(infoAdicionalContenedor != null)
                        {
                            NodeList detAdicionalList = infoAdicionalContenedor.getElementsByTagName("detAdicional");
                            for(int k = 0 ; k< detAdicionalList.getLength();k++)
                            {   
                                Element detAdicional = (Element)detAdicionalList.item(k);
                                detalleFactura.getDetallesAdicionales().getDetalleAdicional().add(new DetalleAdicional(((Node)detAdicional).getAttributes().getNamedItem("nombre").getTextContent(), ((Node)detAdicional).getAttributes().getNamedItem("valor").getTextContent()));
                            }
                        }
                    }

                    Element impuestosContenedor = (Element)detalle.getElementsByTagName("impuestos").item(0);
                    NodeList impuestosList = impuestosContenedor.getElementsByTagName("impuesto");
                    for(int k = 0 ; k< impuestosList.getLength();k++)
                    {
                        Element impuesto = (Element)impuestosList.item(k);
                        Impuesto impuestoDetalle = new Impuesto(
                                impuesto.getElementsByTagName("codigo").item(0).getTextContent(), 
                                impuesto.getElementsByTagName("codigoPorcentaje").item(0).getTextContent(),
                                impuesto.getElementsByTagName("tarifa").item(0).getTextContent(), 
                                impuesto.getElementsByTagName("baseImponible").item(0).getTextContent(), 
                                impuesto.getElementsByTagName("valor").item(0).getTextContent());
                        detalleFactura.getDetallesImpuestos().getDetalleImpuesto().add(impuestoDetalle);
                    }

                    DetallesFactura detallesFactura =  factura.getDetallesFactura();
                    detallesFactura.getDetalleFactura().add(detalleFactura);
                    factura.setDetallesFactura(detallesFactura);
                }

                if(doc.getElementsByTagName("infoAdicional") !=null)
                {
                    Element infoAdicionalContenedor = (Element)doc.getElementsByTagName("infoAdicional").item(0);
                    if(infoAdicionalContenedor != null)
                    {
                        NodeList campoAdicionalList = infoAdicionalContenedor.getElementsByTagName("campoAdicional");
                        for(int j = 0 ; j< campoAdicionalList.getLength();j++)
                        {   
                            Element infoAdicional = (Element)campoAdicionalList.item(j);
                            factura.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(((Node)infoAdicional).getAttributes().getNamedItem("nombre").getTextContent(), ((Node)infoAdicional).getTextContent()));
                        }
                    }
                }
                
                

                QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
                queueDataProcessor.setFileCabecera(fileCabecera);
                queueDataProcessor.setDefaultComprobanteElectronico(factura);
                queueDataProcessor.setDocumentW3C(doc);
                queueDataProcessor.setTipoIntegracion("XMLSF");
                
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(doc.getElementsByTagName("codDoc").item(0).getTextContent().equals("04"))
            {
//                    CreditoExtendido credito = new CreditoExtendido();
//                    credito.setDocumentoVersion("1.0.0");
//                    credito.setCodigoAmbiente(cabeceraCSV[1][0]);
//                    credito.setCodigoTipoEmision(cabeceraCSV[1][1]);
//                    credito.setRazonSocial(cabeceraCSV[1][2]);
//                    credito.setNombreComercial(cabeceraCSV[1][3]);
//                    credito.setRuc(cabeceraCSV[1][4]);
//                    credito.setClaveAcceso(cabeceraCSV[1][5]);
//                    credito.setCodigoDocumento(cabeceraCSV[1][6]);
//                    credito.setEstablecimiento(cabeceraCSV[1][7]);
//                    credito.setPuntoEmision(cabeceraCSV[1][8]);
//                    credito.setSecuencial(cabeceraCSV[1][9]);
//                    credito.setDireccionMatriz(cabeceraCSV[1][10]);
//                    credito.setFechaEmision(cabeceraCSV[1][11]);
//                    credito.setDireccionEstablecimiento(cabeceraCSV[1][12]);
//                    credito.setTipoIdentificacionDelComprador(cabeceraCSV[1][13]);
//                    credito.setRazonSocialDelComprador(cabeceraCSV[1][14]);
//                    credito.setIdentificacionDelComprador(cabeceraCSV[1][15]);
//                    credito.setContribuyenteEspecial(cabeceraCSV[1][16]);
//                    credito.setObligadoALlebarContabilidad(cabeceraCSV[1][17]);
//                    credito.setRise(cabeceraCSV[1][18]);
//                    credito.setCodDocModificado(cabeceraCSV[1][19]);
//                    credito.setNumDocModificado(cabeceraCSV[1][20]);
//                    credito.setFechaEmisionDocSustento(cabeceraCSV[1][21]);
//                    credito.setTotalSinImpuestos(cabeceraCSV[1][22]);
//
//                    credito.setValorModificacion(cabeceraCSV[1][23]);
//
//                    credito.setMoneda(cabeceraCSV[1][24]);
//
//                    for(int j = 1 ; j< detalleCSV.length;j++)
//                    {
//                        DetalleCredito detalleCredito = new DetalleCredito(detalleCSV[j][0], detalleCSV[j][1], detalleCSV[j][2], detalleCSV[j][3], detalleCSV[j][4], detalleCSV[j][5]);
//                        //Container.getListLogger().get(0).log(Level.INFO,TaskID + " - " +"longitud: " + detalleCSV[j].length);
//                        if(detalleCSV[j].length >= 11)
//                        {
//                            if(!detalleCSV[j][6].trim().equals(""))
//                            {
//                                Impuesto impuestoDetalle = new Impuesto(detalleCSV[j][6], detalleCSV[j][7],detalleCSV[j][8], detalleCSV[j][9], detalleCSV[j][10]);
//                                detalleCredito.getImpuestosDetalle().getImpuestoDetalle().add(impuestoDetalle);
//                            }
//                        }
//                        if(detalleCSV[j].length >= 16)
//                        {
//                            if(!detalleCSV[j][11].trim().equals(""))
//                            {
//                                Impuesto impuestoDetalle = new Impuesto(detalleCSV[j][11], detalleCSV[j][12],detalleCSV[j][13], detalleCSV[j][14], detalleCSV[j][15]);
//                                detalleCredito.getImpuestosDetalle().getImpuestoDetalle().add(impuestoDetalle);
//                            }
//                        }
//
//                        DetallesCredito detallesCredito =  credito.getDetallesCredito();
//                        detallesCredito.getDetalleCredito().add(detalleCredito);
//                        credito.setDetallesCredito(detallesCredito);
//                    }
//
//                    credito.setMotivo(cabeceraCSV[1][25]);
//
//                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"cabeceraCSV[1].length="+ cabeceraCSV[1].length);
//                    if(cabeceraCSV[1].length > 26)
//                    {
//                        credito.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional("email", cabeceraCSV[1][26].trim()));
//                    }
//                    
                QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
                queueDataProcessor.setFileCabecera(fileCabecera);
//                queueDataProcessor.setDefaultComprobanteElectronico(credito);
                queueDataProcessor.setDocumentW3C(doc);

//                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(doc.getElementsByTagName("codDoc").item(0).getTextContent().equals("05"))
            {
//                    DebitoExtendido debito = new DebitoExtendido();
//                    debito.setDocumentoVersion("1.0.0");
//                    debito.setCodigoAmbiente(cabeceraCSV[1][0]);
//                    debito.setCodigoTipoEmision(cabeceraCSV[1][1]);
//                    debito.setRazonSocial(cabeceraCSV[1][2]);
//                    debito.setNombreComercial(cabeceraCSV[1][3]);
//                    debito.setRuc(cabeceraCSV[1][4]);
//                    debito.setClaveAcceso(cabeceraCSV[1][5]);
//                    debito.setCodigoDocumento(cabeceraCSV[1][6]);
//                    debito.setEstablecimiento(cabeceraCSV[1][7]);
//                    debito.setPuntoEmision(cabeceraCSV[1][8]);
//                    debito.setSecuencial(cabeceraCSV[1][9]);
//                    debito.setDireccionMatriz(cabeceraCSV[1][10]);
//                    debito.setFechaEmision(cabeceraCSV[1][11]);
//                    debito.setDireccionEstablecimiento(cabeceraCSV[1][12]);
//                    debito.setTipoIdentificacionDelComprador(cabeceraCSV[1][13]);
//                    debito.setRazonSocialDelComprador(cabeceraCSV[1][14]);
//                    debito.setIdentificacionDelComprador(cabeceraCSV[1][15]);
//                    debito.setContribuyenteEspecial(cabeceraCSV[1][16]);
//                    debito.setObligadoALlebarContabilidad(cabeceraCSV[1][17]);
//                    debito.setRise(cabeceraCSV[1][18]);
//                    debito.setCodDocModificado(cabeceraCSV[1][19]);
//                    debito.setNumDocModificado(cabeceraCSV[1][20]);
//                    debito.setFechaEmisionDocSustento(cabeceraCSV[1][21]);
//                    debito.setTotalSinImpuestos(cabeceraCSV[1][22]);
//
//                    for(int j = 1 ; j< detalleCSV.length;j++)
//                    {
//                        MotivoDebito motivoDebito = new MotivoDebito(detalleCSV[j][0], detalleCSV[j][1]);
//                        //Container.getListLogger().get(0).log(Level.INFO,TaskID + " - " +"longitud: " + detalleCSV[j].length);
//                        if(detalleCSV[j].length >= 7)
//                        {
//                            if(!detalleCSV[j][2].trim().equals(""))
//                            {
//                                Impuesto impuestoDetalle = new Impuesto(detalleCSV[j][2], detalleCSV[j][3],detalleCSV[j][4], detalleCSV[j][5], detalleCSV[j][6]);
//                                motivoDebito.getMotivosImpuestos().getMotivoImpuesto().add(impuestoDetalle);
//                            }
//                        }
//
//                        MotivosDebito motivosDebito =  debito.getMotivosDebito();
//                        motivosDebito.getMotivoDebito().add(motivoDebito);
//                        debito.setMotivosDebito(motivosDebito);
//                    }
//
//                    debito.setValorTotal(cabeceraCSV[1][23]);
//
//                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"cabeceraCSV[1].length="+ cabeceraCSV[1].length);
//                    if(cabeceraCSV[1].length > 24)
//                    {
//                        debito.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional("email", cabeceraCSV[1][24].trim()));
//                    }
//                    
//                    QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
//                    queueDataProcessor.setFileCabecera(fileCabecera);
//                    queueDataProcessor.setFileDetalle(fileDetalle);
//                    queueDataProcessor.setDefaultComprobanteElectronico(debito);
//                    
//                    queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(doc.getElementsByTagName("codDoc").item(0).getTextContent().equals("06"))
            {
                
                Remision remision = new Remision();
                remision.setDocumentoVersion("1.1.0");
                remision.setCodigoAmbiente(doc.getElementsByTagName("ambiente").item(0).getTextContent());
                remision.setCodigoTipoEmision(doc.getElementsByTagName("tipoEmision").item(0).getTextContent());
                remision.setRazonSocial(doc.getElementsByTagName("razonSocial").item(0).getTextContent());
                remision.setNombreComercial(doc.getElementsByTagName("nombreComercial").item(0).getTextContent());
                remision.setRuc(doc.getElementsByTagName("ruc").item(0).getTextContent());
                remision.setClaveAcceso(doc.getElementsByTagName("claveAcceso").item(0).getTextContent());
                remision.setCodigoDocumento(doc.getElementsByTagName("codDoc").item(0).getTextContent());
                remision.setEstablecimiento(doc.getElementsByTagName("estab").item(0).getTextContent());
                remision.setPuntoEmision(doc.getElementsByTagName("ptoEmi").item(0).getTextContent());
                remision.setSecuencial(doc.getElementsByTagName("secuencial").item(0).getTextContent());
                remision.setDireccionMatriz(doc.getElementsByTagName("dirMatriz").item(0).getTextContent());
                //remision.setFechaEmision(doc.getElementsByTagName("fechaEmision").item(0).getTextContent());
                remision.setDireccionEstablecimiento(doc.getElementsByTagName("dirEstablecimiento").item(0).getTextContent());

                remision.setDirPartida(doc.getElementsByTagName("dirPartida").item(0).getTextContent());
                remision.setRazonSocialTransportista(doc.getElementsByTagName("razonSocialTransportista").item(0).getTextContent());
                remision.setTipoIdentificacionTransportista(doc.getElementsByTagName("tipoIdentificacionTransportista").item(0).getTextContent());
                remision.setRucTransportista(doc.getElementsByTagName("rucTransportista").item(0).getTextContent());
                //remision.setRise(cabeceraCSV[1][17]);

                remision.setObligadoALlebarContabilidad(doc.getElementsByTagName("obligadoContabilidad").item(0).getTextContent());

                if(doc.getElementsByTagName("contribuyenteEspecial").item(0) != null)
                {
                    remision.setContribuyenteEspecial(doc.getElementsByTagName("contribuyenteEspecial").item(0).getTextContent());
                }

                remision.setFechaIniTransporte(doc.getElementsByTagName("fechaIniTransporte").item(0).getTextContent());
                remision.setFechaFinTransporte(doc.getElementsByTagName("fechaFinTransporte").item(0).getTextContent());
                remision.setPlaca(doc.getElementsByTagName("placa").item(0).getTextContent());

                Element destinatariosContenedor = (Element)doc.getElementsByTagName("destinatarios").item(0);
                NodeList destinatarioList = destinatariosContenedor.getElementsByTagName("destinatario");
                for(int j = 0 ; j< destinatarioList.getLength();j++)
                {
                    Element destinatario = (Element)destinatarioList.item(j);

                    DestinatarioRemision destinatarioRemision = new DestinatarioRemision(
                            destinatario.getElementsByTagName("identificacionDestinatario").item(0).getTextContent(),
                            destinatario.getElementsByTagName("razonSocialDestinatario").item(0).getTextContent(),
                            destinatario.getElementsByTagName("dirDestinatario").item(0).getTextContent(),
                            destinatario.getElementsByTagName("motivoTraslado").item(0).getTextContent(),
                            null,
                            null,
                            destinatario.getElementsByTagName("ruta").item(0).getTextContent(),
                            null,
                            null,
                            null,
                            null);

                    Element detallesContenedor = (Element)destinatario.getElementsByTagName("detalles").item(0);
                    NodeList detalleList = detallesContenedor.getElementsByTagName("detalle");
                    for(int k = 0 ; k< detalleList.getLength();k++)
                    {
                        Element detalle = (Element)detalleList.item(k);
                        DetalleRemision detalleRemision = new DetalleRemision(
                                detalle.getElementsByTagName("codigoInterno").item(0).getTextContent(), 
                                null, 
                                detalle.getElementsByTagName("descripcion").item(0).getTextContent(),
                                detalle.getElementsByTagName("cantidad").item(0).getTextContent());
                        destinatarioRemision.getDetallesRemision().getDetalleRemision().add(detalleRemision);
                    }

                    remision.getDestinatarios().getDestinatarioRemision().add(destinatarioRemision);
                }

                if(doc.getElementsByTagName("infoAdicional") !=null)
                {
                    Element infoAdicionalContenedor = (Element)doc.getElementsByTagName("infoAdicional").item(0);
                    if(infoAdicionalContenedor != null)
                    {
                        NodeList campoAdicionalList = infoAdicionalContenedor.getElementsByTagName("campoAdicional");
                        for(int j = 0 ; j< campoAdicionalList.getLength();j++)
                        {   
                            Element infoAdicional = (Element)campoAdicionalList.item(j);
                            remision.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(((Node)infoAdicional).getAttributes().getNamedItem("nombre").getTextContent(), ((Node)infoAdicional).getTextContent()));
                        }
                    }
                }
                    
                QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
                queueDataProcessor.setFileCabecera(fileCabecera);
                queueDataProcessor.setDefaultComprobanteElectronico(remision);
                queueDataProcessor.setDocumentW3C(doc);
                queueDataProcessor.setTipoIntegracion("XMLSF");
                
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(doc.getElementsByTagName("codDoc").item(0).getTextContent().equals("07"))
            {
                
                Retencion retencion = new Retencion();
                retencion.setDocumentoVersion("1.0.0");
                retencion.setCodigoAmbiente(doc.getElementsByTagName("ambiente").item(0).getTextContent());
                retencion.setCodigoTipoEmision(doc.getElementsByTagName("tipoEmision").item(0).getTextContent());
                retencion.setRazonSocial(doc.getElementsByTagName("razonSocial").item(0).getTextContent());
                retencion.setNombreComercial(doc.getElementsByTagName("nombreComercial").item(0).getTextContent());
                retencion.setRuc(doc.getElementsByTagName("ruc").item(0).getTextContent());
                retencion.setClaveAcceso(doc.getElementsByTagName("claveAcceso").item(0).getTextContent());
                retencion.setCodigoDocumento(doc.getElementsByTagName("codDoc").item(0).getTextContent());
                retencion.setEstablecimiento(doc.getElementsByTagName("estab").item(0).getTextContent());
                retencion.setPuntoEmision(doc.getElementsByTagName("ptoEmi").item(0).getTextContent());
                retencion.setSecuencial(doc.getElementsByTagName("secuencial").item(0).getTextContent());
                retencion.setDireccionMatriz(doc.getElementsByTagName("dirMatriz").item(0).getTextContent());
                retencion.setFechaEmision(doc.getElementsByTagName("fechaEmision").item(0).getTextContent());
                retencion.setDireccionEstablecimiento(doc.getElementsByTagName("dirEstablecimiento").item(0).getTextContent());
                if(doc.getElementsByTagName("contribuyenteEspecial").item(0) != null)
                {
                    retencion.setContribuyenteEspecial(doc.getElementsByTagName("contribuyenteEspecial").item(0).getTextContent());
                }
                retencion.setObligadoALlebarContabilidad(doc.getElementsByTagName("obligadoContabilidad").item(0).getTextContent());
                retencion.setTipoIdentificacionSujetoRetenido(doc.getElementsByTagName("tipoIdentificacionSujetoRetenido").item(0).getTextContent());
                retencion.setRazonSocialSujetoRetenido(doc.getElementsByTagName("razonSocialSujetoRetenido").item(0).getTextContent());
                retencion.setIdentificacionSujetoRetenido(doc.getElementsByTagName("identificacionSujetoRetenido").item(0).getTextContent());
                retencion.setPeriodoFiscal(doc.getElementsByTagName("periodoFiscal").item(0).getTextContent());

                Element ImpuestosContenedor = (Element)doc.getElementsByTagName("impuestos").item(0);
                NodeList impuestosList = ImpuestosContenedor.getElementsByTagName("impuesto");

                for(int j = 0 ; j< impuestosList.getLength();j++)
                {
                    Element impuesto = (Element)impuestosList.item(j);

                    ImpuestoRetencion impuestoRetencion = new ImpuestoRetencion(
                            impuesto.getElementsByTagName("codigo").item(0).getTextContent(), 
                            impuesto.getElementsByTagName("codigoRetencion").item(0).getTextContent(), 
                            impuesto.getElementsByTagName("baseImponible").item(0).getTextContent(), 
                            impuesto.getElementsByTagName("porcentajeRetener").item(0).getTextContent(), 
                            impuesto.getElementsByTagName("valorRetenido").item(0).getTextContent(), 
                            impuesto.getElementsByTagName("codDocSustento").item(0).getTextContent(), 
                            impuesto.getElementsByTagName("numDocSustento").item(0).getTextContent(), 
                            impuesto.getElementsByTagName("fechaEmisionDocSustento").item(0).getTextContent() 
                    );

                    retencion.getImpuestosRetencion().getImpuestoRetencion().add(impuestoRetencion);
                }

                if(doc.getElementsByTagName("infoAdicional") !=null)
                {
                    Element infoAdicionalContenedor = (Element)doc.getElementsByTagName("infoAdicional").item(0);
                    if(infoAdicionalContenedor != null)
                    {
                        NodeList campoAdicionalList = infoAdicionalContenedor.getElementsByTagName("campoAdicional");
                        for(int j = 0 ; j< campoAdicionalList.getLength();j++)
                        {   
                            Element infoAdicional = (Element)campoAdicionalList.item(j);
                            retencion.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(((Node)infoAdicional).getAttributes().getNamedItem("nombre").getTextContent(), ((Node)infoAdicional).getTextContent()));
                        }
                    }
                }
                    
                QueueDataProcessor queueDataProcessor = new QueueDataProcessor();
                queueDataProcessor.setFileCabecera(fileCabecera);
                queueDataProcessor.setDefaultComprobanteElectronico(retencion);
                queueDataProcessor.setDocumentW3C(doc);
                queueDataProcessor.setTipoIntegracion("XMLSF");
                
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Error - Tipo de documento invalido: " + doc.getElementsByTagName("codDoc").item(0).getTextContent() + " - " + "El documento no se agrega a la siguiente cola");
            }

        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }
}