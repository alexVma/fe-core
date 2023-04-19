/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.taskProcessor;

import java.util.ArrayList;
import java.util.List;
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
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.FacturaExtendida;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetalleFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetallesFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacionExtendida;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.pagos.PagoFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.pagos.Pago;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.pagos.Pagos;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.DestinatarioRemision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.detalles.DetalleRemision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.impuestos.ImpuestoRetencion;
import org.inprofsolutions.sri.fe.core.taskProcessor.data.QueueDataProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorThread;

/**
 *
 * @author caf
 */
public class XMLObjectGeneratorTask implements Runnable
{
    private QueueProcessorThread queueThreadProcessor = null;
    private QueueDataProcessor queueDataProcessor = null;
    private String facturaCustomVersion = null;
    
    public XMLObjectGeneratorTask(QueueDataProcessor queueDataProcessor, QueueProcessorThread queueThreadProcessor, String facturaCustomVersion) 
    {
        this.queueThreadProcessor = queueThreadProcessor;
        this.queueDataProcessor = queueDataProcessor;
        
        this.facturaCustomVersion = facturaCustomVersion;
    }
    
    @Override
    public void run() 
    {
        String taskID = this.toString().replaceAll("org.inprofsolutions.sri.fe.core.taskProcessor.", "");
        
        try
        {
            String cabecera [][] = queueDataProcessor.getCabeceraArray();
            String detalle [][] = queueDataProcessor.getDetalleArray();
            
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Documento encontrado: "+ org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(cabecera[1][6]) +" - Iniciando proceso de documento en ambiente: " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla5.getLabel(cabecera[1][0]));
            System.out.println("entro a la calse XMLObjectGeneratorTask");
            if(cabecera[1][6].equals("01"))
            {
                if(cabecera[0][15].trim().equals("comercioExterior"))
                {
                    FacturaExportacionExtendida facturaExportacionExtendida = new FacturaExportacionExtendida();
                
                    facturaExportacionExtendida.setDocumentoVersion(facturaCustomVersion);
                    //facturaExportacionExtendida.setDocumentoVersion("1.0.0");

                    facturaExportacionExtendida.setCodigoAmbiente(cabecera[1][0]);
                    facturaExportacionExtendida.setCodigoTipoEmision(cabecera[1][1]);
                    facturaExportacionExtendida.setRazonSocial(cabecera[1][2]);
                    facturaExportacionExtendida.setNombreComercial(cabecera[1][3]);
                    facturaExportacionExtendida.setRuc(cabecera[1][4]);
                    facturaExportacionExtendida.setClaveAcceso(cabecera[1][5]);
                    facturaExportacionExtendida.setCodigoDocumento(cabecera[1][6]);
                    facturaExportacionExtendida.setEstablecimiento(cabecera[1][7]);
                    facturaExportacionExtendida.setPuntoEmision(cabecera[1][8]);
                    facturaExportacionExtendida.setSecuencial(cabecera[1][9]);
                    facturaExportacionExtendida.setDireccionMatriz(cabecera[1][10]);
                    facturaExportacionExtendida.setFechaEmision(cabecera[1][11]);
                    facturaExportacionExtendida.setDireccionEstablecimiento(cabecera[1][12]);
                    facturaExportacionExtendida.setContribuyenteEspecial(cabecera[1][13]);
                    facturaExportacionExtendida.setObligadoALlebarContabilidad(cabecera[1][14]);
                    
                    facturaExportacionExtendida.setComercioExterior(cabecera[1][15]);
                    facturaExportacionExtendida.setIncoTermFactura(cabecera[1][16]);
                    facturaExportacionExtendida.setLugarIncoTerm(cabecera[1][17]);
                    facturaExportacionExtendida.setPaisOrigen(cabecera[1][18]);
                    facturaExportacionExtendida.setPuertoEmbarque(cabecera[1][19]);
                    facturaExportacionExtendida.setPuertoDestino(cabecera[1][20]);
                    facturaExportacionExtendida.setPaisDestino(cabecera[1][21]);
                    facturaExportacionExtendida.setPaisAdquisicion(cabecera[1][22]);
                    
                    facturaExportacionExtendida.setTipoIdentificacionDelComprador(cabecera[1][23]);
                    facturaExportacionExtendida.setGuiaRemision(cabecera[1][24]);
                    facturaExportacionExtendida.setRazonSocialDelComprador(cabecera[1][25]);
                    facturaExportacionExtendida.setIdentificacionDelComprador(cabecera[1][26]);
                    facturaExportacionExtendida.setTotalSinImpuestos(cabecera[1][27]);
                    
                    facturaExportacionExtendida.setIncoTermTotalSinImpuestos(cabecera[1][28]);
                    
                    facturaExportacionExtendida.setTotalDescuento(cabecera[1][29]);

                    facturaExportacionExtendida.setPropina(cabecera[1][30]);
                    facturaExportacionExtendida.setImporteTotal(cabecera[1][31]);
                    facturaExportacionExtendida.setMoneda(cabecera[1][32]);
                    
                    PagoFactura pagoFactura = new PagoFactura();
                    pagoFactura.setFormaPago(cabecera[1][33].trim());
                    pagoFactura.setTotal(cabecera[1][34].trim());
                    pagoFactura.setPlazo(cabecera[1][35].trim());
                    pagoFactura.setUnidadTiempo(cabecera[1][36].trim());
                    
                    facturaExportacionExtendida.getPagosFactura().getPagoFactura().add(pagoFactura);
                    
                    for(int j = 1 ; j< detalle.length;j++)
                    {
                        DetalleFactura detalleFactura = new DetalleFactura(detalle[j][0], detalle[j][1], detalle[j][2], detalle[j][3], detalle[j][4], detalle[j][5]);
                        
                        DetalleAdicional detalleAdicional = new DetalleAdicional("PESO NETO", detalle[j][6]);
                        detalleFactura.getDetallesAdicionales().getDetalleAdicional().add(detalleAdicional);
                        
                        DetalleAdicional detalleAdicional1 = new DetalleAdicional("PESO BRUTO", detalle[j][7]);
                        detalleFactura.getDetallesAdicionales().getDetalleAdicional().add(detalleAdicional1);
                        
                        DetalleAdicional detalleAdicional2 = new DetalleAdicional("PARTIDA ARANCELARIA", detalle[j][8]);
                        detalleFactura.getDetallesAdicionales().getDetalleAdicional().add(detalleAdicional2);
                        
                        if(detalle[j].length >= 14)
                        {
                            if(!detalle[j][9].trim().equals(""))//6
                            {
                                Impuesto impuestoDetalle = new Impuesto(detalle[j][9], detalle[j][10],detalle[j][11], detalle[j][12], detalle[j][13]);
                                detalleFactura.getDetallesImpuestos().getDetalleImpuesto().add(impuestoDetalle);
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"][6] = \"\"");
                            }
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length = " + detalle[j].length);
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length < 11; No se agregara IVA");
                        }

                        if(detalle[j].length >= 19)
                        {
                            if(!detalle[j][14].trim().equals(""))
                            {
                                Impuesto impuestoDetalle = new Impuesto(detalle[j][14], detalle[j][15],detalle[j][16], detalle[j][17], detalle[j][18]);
                                detalleFactura.getDetallesImpuestos().getDetalleImpuesto().add(impuestoDetalle);
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"][11] = \"\"");
                            }
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length = " + detalle[j].length);
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length < 16; No se agregara ICE");
                        }

                        DetallesFactura detallesFactura =  facturaExportacionExtendida.getDetallesFactura();
                        detallesFactura.getDetalleFactura().add(detalleFactura);
                        facturaExportacionExtendida.setDetallesFactura(detallesFactura);
                    }

                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"cabecera[1].length="+ cabecera[1].length);
                    if(cabecera[1].length > 37)
                    {
                        for(int i = 37; i < cabecera[1].length; i++)
                        {
                            facturaExportacionExtendida.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(cabecera[0][i].trim().split(":")[1], cabecera[1][i].trim()));
                        }
                    }

                    queueDataProcessor.setDefaultComprobanteElectronico(facturaExportacionExtendida);
                    queueThreadProcessor.addDataToOutdata(queueDataProcessor);
                }
                else
                {
                    FacturaExtendida factura = new FacturaExtendida();
                
                    factura.setDocumentoVersion(facturaCustomVersion);
                    //factura.setDocumentoVersion("1.0.0");
                    
                    factura.setCodigoAmbiente(cabecera[1][0]);
                    factura.setCodigoTipoEmision(cabecera[1][1]);
                    factura.setRazonSocial(cabecera[1][2]);
                    factura.setTotalSubsidio("0");
                    factura.setNombreComercial(cabecera[1][3]);
                    factura.setRuc(cabecera[1][4]);
                    factura.setClaveAcceso(cabecera[1][5]);
                    factura.setCodigoDocumento(cabecera[1][6]);
                    factura.setEstablecimiento(cabecera[1][7]);
                    factura.setPuntoEmision(cabecera[1][8]);
                    factura.setSecuencial(cabecera[1][9]);
                    factura.setDireccionMatriz(cabecera[1][10]);
                    factura.setFechaEmision(cabecera[1][11]);
                    factura.setDireccionEstablecimiento(cabecera[1][12]);
                    factura.setContribuyenteEspecial(cabecera[1][13]);
                    factura.setObligadoALlebarContabilidad(cabecera[1][14]);
                    factura.setTipoIdentificacionDelComprador(cabecera[1][15]);
                    factura.setGuiaRemision(cabecera[1][16]);
                    factura.setRazonSocialDelComprador(cabecera[1][17]);
                    factura.setIdentificacionDelComprador(cabecera[1][18]);
                    factura.setTotalSinImpuestos(cabecera[1][19]);
                    factura.setTotalDescuento(cabecera[1][20]);

                    factura.setPropina(cabecera[1][21]);
                    factura.setImporteTotal(cabecera[1][22]);
                    factura.setMoneda(cabecera[1][23]);
                    for(int j = 1 ; j< detalle.length;j++)
                    {
                        DetalleFactura detalleFactura = new DetalleFactura(detalle[j][0], detalle[j][1], detalle[j][2], detalle[j][3], detalle[j][4], detalle[j][5]);

                        if(detalle[j].length >= 11)
                        {
                            if(!detalle[j][6].trim().equals(""))
                            {
                                Impuesto impuestoDetalle = new Impuesto(detalle[j][6], detalle[j][7],detalle[j][8], detalle[j][9], detalle[j][10]);
                                detalleFactura.getDetallesImpuestos().getDetalleImpuesto().add(impuestoDetalle);
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"][6] = \"\"");
                            }
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length = " + detalle[j].length);
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length < 11; No se agregara IVA");
                        }

                        if(detalle[j].length >= 16)
                        {
                            if(!detalle[j][11].trim().equals(""))
                            {
                                Impuesto impuestoDetalle = new Impuesto(detalle[j][11], detalle[j][12],detalle[j][13], detalle[j][14], detalle[j][15]);
                                detalleFactura.getDetallesImpuestos().getDetalleImpuesto().add(impuestoDetalle);
                            }
                            else
                            {
                                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"][11] = \"\"");
                            }
                        }
                        else
                        {
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length = " + detalle[j].length);
                            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"detalle["+j+"].length < 16; No se agregara ICE");
                        }

                        DetallesFactura detallesFactura =  factura.getDetallesFactura();
                        detallesFactura.getDetalleFactura().add(detalleFactura);
                        factura.setDetallesFactura(detallesFactura);
                    }

                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"cabecera[1].length="+ cabecera[1].length);
                    if(cabecera[1].length > 28)
                    {
                        for(int i = 28; i < cabecera[1].length; i++)
                        {
                            factura.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional(cabecera[0][i].trim().split(":")[1], cabecera[1][i].trim()));
                        }
                    }
                    
                                        ///////////////////////////////////////////////////se agrega pagos //////////////////////////////////////////////////
                    Pagos pagos=new Pagos();
                    List<Pago> allPagos=new ArrayList<>();
                    Pago pago=new Pago();
                    pago.setFormaPago(cabecera[1][24]);
                    pago.setTotal(cabecera[1][25]);
                    pago.setPlazo(cabecera[1][26]);
                    pago.setUnidadTiempo(cabecera[1][27]);
                    allPagos.add(pago);
                    pagos.setPago(allPagos);
                    factura.setPagos(pagos);
                    //////////////////////////////////////////////////se agrega pagos //////////////////////////////////////////////////

                    queueDataProcessor.setDefaultComprobanteElectronico(factura);
                    queueThreadProcessor.addDataToOutdata(queueDataProcessor);
                }
            }
            else if(cabecera[1][6].equals("04"))
            {
                CreditoExtendido credito = new CreditoExtendido();
                credito.setDocumentoVersion("1.0.0");
                credito.setCodigoAmbiente(cabecera[1][0]);
                credito.setCodigoTipoEmision(cabecera[1][1]);
                credito.setRazonSocial(cabecera[1][2]);
                credito.setNombreComercial(cabecera[1][3]);
                credito.setRuc(cabecera[1][4]);
                credito.setClaveAcceso(cabecera[1][5]);
                credito.setCodigoDocumento(cabecera[1][6]);
                credito.setEstablecimiento(cabecera[1][7]);
                credito.setPuntoEmision(cabecera[1][8]);
                credito.setSecuencial(cabecera[1][9]);
                credito.setDireccionMatriz(cabecera[1][10]);
                credito.setFechaEmision(cabecera[1][11]);
                credito.setDireccionEstablecimiento(cabecera[1][12]);
                credito.setTipoIdentificacionDelComprador(cabecera[1][13]);
                credito.setRazonSocialDelComprador(cabecera[1][14]);
                credito.setIdentificacionDelComprador(cabecera[1][15]);
                credito.setContribuyenteEspecial(cabecera[1][16]);
                credito.setObligadoALlebarContabilidad(cabecera[1][17]);
                credito.setRise(cabecera[1][18]);
                credito.setCodDocModificado(cabecera[1][19]);
                credito.setNumDocModificado(cabecera[1][20]);
                credito.setFechaEmisionDocSustento(cabecera[1][21]);
                credito.setTotalSinImpuestos(cabecera[1][22]);

                credito.setValorModificacion(cabecera[1][23]);

                credito.setMoneda(cabecera[1][24]);

                for(int j = 1 ; j< detalle.length;j++)
                {
                    DetalleCredito detalleCredito = new DetalleCredito(detalle[j][0], detalle[j][1], detalle[j][2], detalle[j][3], detalle[j][4], detalle[j][5]);
                    //Container.getListLogger().get(0).log(Level.INFO,TaskID + " - " +"longitud: " + detalle[j].length);
                    if(detalle[j].length >= 11)
                    {
                        if(!detalle[j][6].trim().equals(""))
                        {
                            Impuesto impuestoDetalle = new Impuesto(detalle[j][6], detalle[j][7],detalle[j][8], detalle[j][9], detalle[j][10]);
                            detalleCredito.getImpuestosDetalle().getImpuestoDetalle().add(impuestoDetalle);
                        }
                    }
                    if(detalle[j].length >= 16)
                    {
                        if(!detalle[j][11].trim().equals(""))
                        {
                            Impuesto impuestoDetalle = new Impuesto(detalle[j][11], detalle[j][12],detalle[j][13], detalle[j][14], detalle[j][15]);
                            detalleCredito.getImpuestosDetalle().getImpuestoDetalle().add(impuestoDetalle);
                        }
                    }

                    DetallesCredito detallesCredito =  credito.getDetallesCredito();
                    detallesCredito.getDetalleCredito().add(detalleCredito);
                    credito.setDetallesCredito(detallesCredito);
                }

                credito.setMotivo(cabecera[1][25]);

                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"cabecera[1].length="+ cabecera[1].length);
                if(cabecera[1].length > 26)
                {
                    credito.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional("email", cabecera[1][26].trim()));
                }

                queueDataProcessor.setDefaultComprobanteElectronico(credito);
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(cabecera[1][6].equals("05"))
            {
                DebitoExtendido debito = new DebitoExtendido();
                debito.setDocumentoVersion("1.0.0");
                debito.setCodigoAmbiente(cabecera[1][0]);
                debito.setCodigoTipoEmision(cabecera[1][1]);
                debito.setRazonSocial(cabecera[1][2]);
                debito.setNombreComercial(cabecera[1][3]);
                debito.setRuc(cabecera[1][4]);
                debito.setClaveAcceso(cabecera[1][5]);
                debito.setCodigoDocumento(cabecera[1][6]);
                debito.setEstablecimiento(cabecera[1][7]);
                debito.setPuntoEmision(cabecera[1][8]);
                debito.setSecuencial(cabecera[1][9]);
                debito.setDireccionMatriz(cabecera[1][10]);
                debito.setFechaEmision(cabecera[1][11]);
                debito.setDireccionEstablecimiento(cabecera[1][12]);
                debito.setTipoIdentificacionDelComprador(cabecera[1][13]);
                debito.setRazonSocialDelComprador(cabecera[1][14]);
                debito.setIdentificacionDelComprador(cabecera[1][15]);
                debito.setContribuyenteEspecial(cabecera[1][16]);
                debito.setObligadoALlebarContabilidad(cabecera[1][17]);
                debito.setRise(cabecera[1][18]);
                debito.setCodDocModificado(cabecera[1][19]);
                debito.setNumDocModificado(cabecera[1][20]);
                debito.setFechaEmisionDocSustento(cabecera[1][21]);
                debito.setTotalSinImpuestos(cabecera[1][22]);

                for(int j = 1 ; j< detalle.length;j++)
                {
                    MotivoDebito motivoDebito = new MotivoDebito(detalle[j][0], detalle[j][1]);
                    //Container.getListLogger().get(0).log(Level.INFO,TaskID + " - " +"longitud: " + detalle[j].length);
                    if(detalle[j].length >= 7)
                    {
                        if(!detalle[j][2].trim().equals(""))
                        {
                            Impuesto impuestoDetalle = new Impuesto(detalle[j][2], detalle[j][3],detalle[j][4], detalle[j][5], detalle[j][6]);
                            motivoDebito.getMotivosImpuestos().getMotivoImpuesto().add(impuestoDetalle);
                        }
                    }

                    MotivosDebito motivosDebito =  debito.getMotivosDebito();
                    motivosDebito.getMotivoDebito().add(motivoDebito);
                    debito.setMotivosDebito(motivosDebito);
                }

                debito.setValorTotal(cabecera[1][23]);

                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"cabecera[1].length="+ cabecera[1].length);
                if(cabecera[1].length > 24)
                {
                    debito.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional("email", cabecera[1][24].trim()));
                }

                queueDataProcessor.setDefaultComprobanteElectronico(debito);
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(cabecera[1][6].equals("06"))
            {
                Remision remision = new Remision();
                remision.setDocumentoVersion("1.0.0");
                remision.setCodigoAmbiente(cabecera[1][0]);
                remision.setCodigoTipoEmision(cabecera[1][1]);
                remision.setRazonSocial(cabecera[1][2]);
                remision.setNombreComercial(cabecera[1][3]);
                remision.setRuc(cabecera[1][4]);
                remision.setClaveAcceso(cabecera[1][5]);
                remision.setCodigoDocumento(cabecera[1][6]);
                remision.setEstablecimiento(cabecera[1][7]);
                remision.setPuntoEmision(cabecera[1][8]);
                remision.setSecuencial(cabecera[1][9]);
                remision.setDireccionMatriz(cabecera[1][10]);
                remision.setFechaEmision(cabecera[1][11]);
                remision.setDireccionEstablecimiento(cabecera[1][12]);
                remision.setDirPartida(cabecera[1][13]);
                remision.setRazonSocialTransportista(cabecera[1][14]);
                remision.setTipoIdentificacionTransportista(cabecera[1][15]);
                remision.setRucTransportista(cabecera[1][16]);
                remision.setRise(cabecera[1][17]);
                remision.setObligadoALlebarContabilidad(cabecera[1][18]);
                remision.setContribuyenteEspecial(cabecera[1][19]);
                remision.setFechaIniTransporte(cabecera[1][20]);
                remision.setFechaFinTransporte(cabecera[1][21]);
                remision.setPlaca(cabecera[1][22]);

                for(int j = 1 ; j< detalle.length;j++)
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Destinatario j = " + j);
                    DestinatarioRemision destinatarioRemision = new DestinatarioRemision(detalle[j][0], detalle[j][1], detalle[j][2], detalle[j][3], detalle[j][4], detalle[j][5], detalle[j][6], detalle[j][7], detalle[j][8], detalle[j][9], detalle[j][10]);
                    DetalleRemision detalleRemision = new DetalleRemision(detalle[j][11], detalle[j][12], detalle[j][13], detalle[j][14]);
                    DetalleAdicional detalleAdicional = new DetalleAdicional();
                    detalleAdicional.setNombre(detalle[j][15]);
                    detalleAdicional.setValor(detalle[j][16]);

                    detalleRemision.getDetallesAdicionales().getDetalleAdicional().add(detalleAdicional);

                    destinatarioRemision.getDetallesRemision().getDetalleRemision().add(detalleRemision);

                    LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"remision.getDestinatarios().getDestinatarioRemision().size() = " + remision.getDestinatarios().getDestinatarioRemision().size());
                    if(remision.getDestinatarios().getDestinatarioRemision().size() == 0)
                    {
                        remision.getDestinatarios().getDestinatarioRemision().add(destinatarioRemision);
                    }
                    else
                    {
                        for(int k = 0 ; k < remision.getDestinatarios().getDestinatarioRemision().size() ; k++)
                        {
                            DestinatarioRemision destinatarioRemisionRegistrado = remision.getDestinatarios().getDestinatarioRemision().get(k);

                            if(
                                    (destinatarioRemisionRegistrado.getIdentificacionDestinatario().equals(destinatarioRemision.getIdentificacionDestinatario())) &
                                    (destinatarioRemisionRegistrado.getRazonSocialDestinatario().equals(destinatarioRemision.getRazonSocialDestinatario())) &
                                    (destinatarioRemisionRegistrado.getDirDestinatario().equals(destinatarioRemision.getDirDestinatario())) &
                                    (destinatarioRemisionRegistrado.getMotivoTraslado().equals(destinatarioRemision.getMotivoTraslado())) &
                                    (destinatarioRemisionRegistrado.getDocAduaneroUnico().equals(destinatarioRemision.getDocAduaneroUnico())) &
                                    (destinatarioRemisionRegistrado.getCodEstabDestino().equals(destinatarioRemision.getCodEstabDestino())) &
                                    (destinatarioRemisionRegistrado.getRuta().equals(destinatarioRemision.getRuta())) &
                                    (destinatarioRemisionRegistrado.getCodDocSustento().equals(destinatarioRemision.getCodDocSustento())) &
                                    (destinatarioRemisionRegistrado.getNumDocSustento().equals(destinatarioRemision.getNumDocSustento())) &
                                    (destinatarioRemisionRegistrado.getNumAutDocSustento().equals(destinatarioRemision.getNumAutDocSustento())) &
                                    (destinatarioRemisionRegistrado.getFechaEmisionDocSustento().equals(destinatarioRemision.getFechaEmisionDocSustento()))
                                    )
                            {

                                destinatarioRemisionRegistrado.getDetallesRemision().getDetalleRemision().add(detalleRemision);
                                k = remision.getDestinatarios().getDestinatarioRemision().size();
                            }
                            else
                            {
                                if(k == remision.getDestinatarios().getDestinatarioRemision().size()-1)
                                {
                                    remision.getDestinatarios().getDestinatarioRemision().add(destinatarioRemision);
                                    k = remision.getDestinatarios().getDestinatarioRemision().size();
                                }
                            }
                        }
                    }
                }

                if(cabecera[1].length > 23)
                {
                    remision.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional("email", cabecera[1][23].trim()));
                }

                queueDataProcessor.setDefaultComprobanteElectronico(remision);
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else if(cabecera[1][6].equals("07"))
            {
                Retencion retencion = new Retencion();
                retencion.setDocumentoVersion("1.0.0");
                retencion.setCodigoAmbiente(cabecera[1][0]);
                retencion.setCodigoTipoEmision(cabecera[1][1]);
                retencion.setRazonSocial(cabecera[1][2]);
                retencion.setNombreComercial(cabecera[1][3]);
                retencion.setRuc(cabecera[1][4]);
                retencion.setClaveAcceso(cabecera[1][5]);
                retencion.setCodigoDocumento(cabecera[1][6]);
                retencion.setEstablecimiento(cabecera[1][7]);
                retencion.setPuntoEmision(cabecera[1][8]);
                retencion.setSecuencial(cabecera[1][9]);
                retencion.setDireccionMatriz(cabecera[1][10]);
                retencion.setFechaEmision(cabecera[1][11]);
                retencion.setDireccionEstablecimiento(cabecera[1][12]);
                retencion.setContribuyenteEspecial(cabecera[1][13]);
                retencion.setObligadoALlebarContabilidad(cabecera[1][14]);
                retencion.setTipoIdentificacionSujetoRetenido(cabecera[1][15]);
                retencion.setRazonSocialSujetoRetenido(cabecera[1][16]);
                retencion.setIdentificacionSujetoRetenido(cabecera[1][17]);
                retencion.setPeriodoFiscal(cabecera[1][18]);

                for(int j = 1 ; j< detalle.length;j++)
                {
                    ImpuestoRetencion impuestoRetencion = new ImpuestoRetencion(detalle[j][0], detalle[j][1], detalle[j][2], detalle[j][3], detalle[j][4], detalle[j][5],detalle[j][6], detalle[j][7]);

                    retencion.getImpuestosRetencion().getImpuestoRetencion().add(impuestoRetencion);
                }

                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"cabecera[1].length="+ cabecera[1].length);
                if(cabecera[1].length > 19)
                {
                    retencion.getInfoAdicional().getCamposAdicionales().add(new CampoAdicional("email", cabecera[1][19].trim()));
                }

                queueDataProcessor.setDefaultComprobanteElectronico(retencion);
                queueThreadProcessor.addDataToOutdata(queueDataProcessor);
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +"Error - Tipo de documento invalido: " + cabecera[1][6] + " - " + "El documento no se agrega a la siguiente cola");
            }
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " +e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }
    
}
