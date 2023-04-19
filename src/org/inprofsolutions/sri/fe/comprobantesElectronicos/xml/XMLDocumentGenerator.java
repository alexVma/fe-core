/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.xml;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.Debito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.inprofsolutions.utils.w3c.dom.DocumentWizard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author caf
 */
public class XMLDocumentGenerator extends DocumentWizard
{

    public XMLDocumentGenerator() throws ParserConfigurationException 
    {}
    
    private Element getInfoTributaria(DefaultComprobanteElectronico comprobanteElectronico)
    {
        Element infoTributaria = doc.createElement("infoTributaria");
        infoTributaria.appendChild(getNodo("ambiente", comprobanteElectronico.getCodigoAmbiente()));
        infoTributaria.appendChild(getNodo("tipoEmision", comprobanteElectronico.getCodigoTipoEmision()));
        infoTributaria.appendChild(getNodo("razonSocial", comprobanteElectronico.getRazonSocial()));      
        if(comprobanteElectronico.getNombreComercial() != null)
        {
            if(!comprobanteElectronico.getNombreComercial().trim().equals(""))
            {
                infoTributaria.appendChild(getNodo("nombreComercial", comprobanteElectronico.getNombreComercial()));
            }
        }
        infoTributaria.appendChild(getNodo("ruc", comprobanteElectronico.getRuc()));
        infoTributaria.appendChild(getNodo("claveAcceso", comprobanteElectronico.getClaveAcceso()));
        infoTributaria.appendChild(getNodo("codDoc", comprobanteElectronico.getCodigoDocumento()));
        infoTributaria.appendChild(getNodo("estab", comprobanteElectronico.getEstablecimiento()));
        infoTributaria.appendChild(getNodo("ptoEmi", comprobanteElectronico.getPuntoEmision()));
        infoTributaria.appendChild(getNodo("secuencial", comprobanteElectronico.getSecuencial()));
        infoTributaria.appendChild(getNodo("dirMatriz", comprobanteElectronico.getDireccionMatriz()));
        
        return infoTributaria;
    }
    
    private void procesadorDeClaveDeAcceso(DefaultComprobanteElectronico comprobanteElectronico) throws Exception
    {
        String PATTERN_NUMBER = ".*[^0-9].*";
        Pattern numberPattern = Pattern.compile(PATTERN_NUMBER);
        
        if(comprobanteElectronico.getCodigoTipoEmision().equals("1"))
        {            
            if(comprobanteElectronico.getClaveAcceso() == null)
            {
                comprobanteElectronico.setClaveAcceso(comprobanteElectronico.generarClaveDeAccesoNormal());
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + "La clave de acceso se ha generado para el documento: " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento()) + " " + comprobanteElectronico.getEstablecimiento()+"-"+comprobanteElectronico.getPuntoEmision()+"-"+comprobanteElectronico.getSecuencial());
            }
            else if(comprobanteElectronico.getClaveAcceso().trim().equals(""))
            {
                comprobanteElectronico.setClaveAcceso(comprobanteElectronico.generarClaveDeAccesoNormal());
                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + "La clave de acceso se ha generado para el documento: " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento()) + " " + comprobanteElectronico.getEstablecimiento()+"-"+comprobanteElectronico.getPuntoEmision()+"-"+comprobanteElectronico.getSecuencial());
            }
//            else if(!numberPattern.matcher(comprobanteElectronico.getClaveAcceso().trim()).matches())
//            {
//                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + "La clave de acceso encontrada no es un numero: " + comprobanteElectronico.getClaveAcceso());
//            }
            else if(comprobanteElectronico.getClaveAcceso().trim().length() == 49)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"La clave de acceso tiene una logitud de: " + comprobanteElectronico.getClaveAcceso().trim().length() + ", se usara esta como clave de acceso de emision normal");
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"La clave de acceso encontrada tiene una logitud de: " + comprobanteElectronico.getClaveAcceso().trim().length() + ", la cual no cumple con los requisitos del SRI");
            }
        }
        else if(comprobanteElectronico.getCodigoTipoEmision().equals("2"))
        {
            if(comprobanteElectronico.getClaveAcceso() == null)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"La clave de contigencia encontrada esta es null, imposible generar la clave de acceso contigente");
            }
            else if(comprobanteElectronico.getClaveAcceso().trim().equals(""))
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"La clave de contigencia encontrada esta esta en blanco, imposible generar la clave de acceso contigente");
            }
//            else if(!numberPattern.matcher(comprobanteElectronico.getClaveAcceso().trim()).matches())
//            {
//                LoggerContainer.getListLogger().get(0).log(Level.INFO,comprobanteElectronico.getClaveAcceso() + " - " + "La clave de acceso encontrada no es un numero: " + comprobanteElectronico.getClaveAcceso());
//            }
            else if(comprobanteElectronico.getClaveAcceso().trim().length() == 37)
            {
                comprobanteElectronico.setClaveAcceso(comprobanteElectronico.generarClaveDeAccesoContigente(comprobanteElectronico.getClaveAcceso().trim()));
            }
            else if(comprobanteElectronico.getClaveAcceso().trim().length() == 49)
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"La clave de contigencia encontrada tiene una logitud de: " + comprobanteElectronico.getClaveAcceso().trim().length() + ", se usara esta como clave de acceso contigente");
            }
            else
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"La clave de contigencia encontrada tiene una logitud de: " + comprobanteElectronico.getClaveAcceso().trim().length() + ", la cual no cumple con los requisitos del SRI");
            }
        }
    }
    
    public String getXMLAutorizacionSRI(String estado, String numeroAutorizacion, String fechaAutorizacion,String ambiente, String comprobante) throws TransformerException, TransformerConfigurationException, SAXException, IOException
    {
        doc = db.newDocument();
        Element elementAutorizacion = doc.createElement("autorizacion");

        elementAutorizacion.appendChild(getNodo("estado",estado));
        elementAutorizacion.appendChild(getNodo("numeroAutorizacion",numeroAutorizacion));
        elementAutorizacion.appendChild(getNodo("fechaAutorizacion",fechaAutorizacion));
        elementAutorizacion.appendChild(getNodo("ambiente",ambiente));
        elementAutorizacion.appendChild(getNodo("comprobante", comprobante));
        Element mensajes = doc.createElement("mensajes");
        elementAutorizacion.appendChild(mensajes);

        doc.appendChild(elementAutorizacion);

        doc.setXmlStandalone(true);

        return getGenerateDocumentAsString("yes","no","yes",3);
    }
    
    public Document getFacturaXML(Factura factura) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, Exception
    {
        getFacturaBaseXML(factura);
        return getGenerateDocument();
    }
    
    public Document getFacturaExportacionXML(FacturaExportacion facturaExportacion) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, Exception
    {
        getFacturaBaseXML(facturaExportacion);
        
        Element infoFacturaContenedor = (Element)doc.getElementsByTagName("infoFactura").item(0);
        Node tipoIdentificacionComprador = infoFacturaContenedor.getElementsByTagName("tipoIdentificacionComprador").item(0);
        infoFacturaContenedor.insertBefore(getNodo("comercioExterior", facturaExportacion.getComercioExterior()), tipoIdentificacionComprador);
        
        infoFacturaContenedor.insertBefore(getNodo("incoTermFactura", facturaExportacion.getIncoTermFactura()), tipoIdentificacionComprador);
        infoFacturaContenedor.insertBefore(getNodo("lugarIncoTerm", facturaExportacion.getLugarIncoTerm()), tipoIdentificacionComprador);
        infoFacturaContenedor.insertBefore(getNodo("paisOrigen", facturaExportacion.getPaisOrigen()), tipoIdentificacionComprador);
        infoFacturaContenedor.insertBefore(getNodo("puertoEmbarque", facturaExportacion.getPuertoEmbarque()), tipoIdentificacionComprador);
        infoFacturaContenedor.insertBefore(getNodo("puertoDestino", facturaExportacion.getPuertoDestino()), tipoIdentificacionComprador);
        
        insertBeforeNodeIfValueIsNotNull("paisDestino", facturaExportacion.getPaisDestino(), infoFacturaContenedor, (Element)tipoIdentificacionComprador);
        insertBeforeNodeIfValueIsNotNull("paisAdquisicion", facturaExportacion.getPaisAdquisicion(), infoFacturaContenedor, (Element)tipoIdentificacionComprador);
        
        Node totalDescuento = infoFacturaContenedor.getElementsByTagName("totalDescuento").item(0);
        infoFacturaContenedor.insertBefore(getNodo("incoTermTotalSinImpuestos", facturaExportacion.getIncoTermTotalSinImpuestos()), totalDescuento);

        Element elementPagos = doc.createElement("pagos");
        for(int i = 0; i < facturaExportacion.getPagosFactura().getPagoFactura().size(); i++)
        {
            Element elementPago = doc.createElement("pago");
            
            elementPago.appendChild(getNodo("formaPago",facturaExportacion.getPagosFactura().getPagoFactura().get(i).getFormaPago()));
            elementPago.appendChild(getNodo("total",facturaExportacion.getPagosFactura().getPagoFactura().get(i).getTotal()));
            elementPago.appendChild(getNodo("plazo", facturaExportacion.getPagosFactura().getPagoFactura().get(i).getPlazo()));
            elementPago.appendChild(getNodo("unidadTiempo", facturaExportacion.getPagosFactura().getPagoFactura().get(i).getUnidadTiempo()));
            elementPagos.appendChild(elementPago);
        }
        infoFacturaContenedor.appendChild(elementPagos);
        
        return getGenerateDocument();
    }
    
    
    public Document getFacturaBaseXML(Factura factura) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, Exception
    {
        DefaultComprobanteElectronico comprobanteElectronico = (DefaultComprobanteElectronico)factura; 
        procesadorDeClaveDeAcceso(comprobanteElectronico);
            
        doc = db.newDocument();

        Element elementFactura = doc.createElement("factura");
        elementFactura.setAttribute("id", "comprobante");
        elementFactura.setAttribute("version", factura.getDocumentoVersion());

        Element infoTributaria = getInfoTributaria(factura);
        Element infoFactura = doc.createElement("infoFactura");//getInfoComprobante(factura,"infoFactura");
        Element elementDetalles = doc.createElement("detalles");
        Element elementInfoAdicional = doc.createElement("infoAdicional");

        infoFactura.appendChild(getNodo("fechaEmision", factura.getFechaEmision()));        
        appendNodeIfValueIsNotNull("dirEstablecimiento", comprobanteElectronico.getDireccionEstablecimiento(), infoFactura);
        if(!comprobanteElectronico.getContribuyenteEspecial().trim().equals("999"))
        appendNodeIfValueIsNotNull("contribuyenteEspecial", comprobanteElectronico.getContribuyenteEspecial(), infoFactura);
            else
        appendNodeIfValueIsNotNull("contribuyenteEspecial","", infoFactura);        
        appendNodeIfValueIsNotNull("obligadoContabilidad", comprobanteElectronico.getObligadoALlebarContabilidad(), infoFactura);
        infoFactura.appendChild(getNodo("tipoIdentificacionComprador", factura.getTipoIdentificacionDelComprador()));        
        //System.out.println("factura.getTipoIdentificacionDelComprador()   si esta bien aqui es donde     "+factura.getTipoIdentificacionDelComprador());
        appendNodeIfValueIsNotNull("guiaRemision", factura.getGuiaRemision(), infoFactura);
        infoFactura.appendChild(getNodo("razonSocialComprador", factura.getRazonSocialDelComprador()));
        infoFactura.appendChild(getNodo("identificacionComprador", factura.getIdentificacionDelComprador()));
        infoFactura.appendChild(getNodo("totalSinImpuestos", factura.getTotalSinImpuestos()));
        infoFactura.appendChild(getNodo("totalSubsidio", factura.getTotalSubsidio()));
        infoFactura.appendChild(getNodo("totalDescuento", factura.getTotalDescuento()));

        Element elementTotalConImpuestos = doc.createElement("totalConImpuestos");
        for(int i = 0; i < factura.getTotalConImpuestos().getImpuesto().size(); i++)
        {
            Element elementTotalImpuesto = doc.createElement("totalImpuesto");
            elementTotalImpuesto.appendChild(getNodo("codigo",factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo()));
            elementTotalImpuesto.appendChild(getNodo("codigoPorcentaje",factura.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje()));
            elementTotalImpuesto.appendChild(getNodo("baseImponible", factura.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible()));
            //elementTotalImpuesto.appendChild(getNodo("tarifa", factura.getTotalConImpuestos(i).getTarifa()));
            elementTotalImpuesto.appendChild(getNodo("valor", factura.getTotalConImpuestos().getImpuesto().get(i).getValor()));
            elementTotalConImpuestos.appendChild(elementTotalImpuesto);
        }
        infoFactura.appendChild(elementTotalConImpuestos);

        infoFactura.appendChild(getNodo("propina", factura.getPropina()));
        infoFactura.appendChild(getNodo("importeTotal", factura.getImporteTotal()));        
        appendNodeIfValueIsNotNull("moneda", factura.getMoneda(), infoFactura);

        for(int i = 0; i < factura.getDetallesFactura().getDetalleFactura().size(); i++)
        {
            System.out.println(factura.getDetallesFactura().getDetalleFactura().get(i).getCodigoAuxiliar()+" "+factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioUnitario()+" "+factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio());
            Element elementDetalle = doc.createElement("detalle");                
            elementDetalle.appendChild(getNodo("codigoPrincipal",factura.getDetallesFactura().getDetalleFactura().get(i).getCodigoPrincipal()));
            appendNodeIfValueIsNotNull("codigoAuxiliar", factura.getDetallesFactura().getDetalleFactura().get(i).getCodigoAuxiliar(), elementDetalle);
            elementDetalle.appendChild(getNodo("descripcion", factura.getDetallesFactura().getDetalleFactura().get(i).getDescripcion()));
            elementDetalle.appendChild(getNodo("cantidad", factura.getDetallesFactura().getDetalleFactura().get(i).getCantidad()));
            elementDetalle.appendChild(getNodo("precioUnitario", factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioUnitario()));
            //elementDetalle.appendChild(getNodo("precioSinSubsidio","0"));// factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio()));
                        //subsidios Double.parseDouble(factura.getImporteTotal()))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
            double cantXvalor= Double.parseDouble(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioUnitario()) * Double.parseDouble(factura.getDetallesFactura().getDetalleFactura().get(i).getCantidad());
            double aux=new BigDecimal("" +Double.parseDouble(""+cantXvalor)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio()!=null)
            if(Double.parseDouble(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio())>aux){
            elementDetalle.appendChild(getNodo("precioSinSubsidio",factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio()));// factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio()));
                }
            elementDetalle.appendChild(getNodo("descuento", factura.getDetallesFactura().getDetalleFactura().get(i).getDescuento()));
            elementDetalle.appendChild(getNodo("precioTotalSinImpuesto", factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioTotalSinImpuesto()));                

            if(factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().size() > 0)
            {
                Element elementDetallesAdicionales = doc.createElement("detallesAdicionales");
                for(int j = 0 ; j < factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().size(); j++)
                {
                    Element detAdicional = getNodo("detAdicional");
                    detAdicional.setAttribute("nombre", factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getNombre());
                    detAdicional.setAttribute("valor", factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getValor());
                    elementDetallesAdicionales.appendChild(detAdicional);
                }
                elementDetalle.appendChild(elementDetallesAdicionales);
            }

            Element elementImpuestos = doc.createElement("impuestos");
            for(int j = 0 ; j < factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesImpuestos().getDetalleImpuesto().size(); j++)
            {
                Element elementImpuesto = doc.createElement("impuesto");
                elementImpuesto.appendChild(getNodo("codigo",factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesImpuestos().getDetalleImpuesto().get(j).getCodigo()));
                elementImpuesto.appendChild(getNodo("codigoPorcentaje", factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesImpuestos().getDetalleImpuesto().get(j).getCodigoPorcentaje()));
                elementImpuesto.appendChild(getNodo("tarifa", factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesImpuestos().getDetalleImpuesto().get(j).getTarifa()));
                elementImpuesto.appendChild(getNodo("baseImponible", factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesImpuestos().getDetalleImpuesto().get(j).getBaseImponible()));
                elementImpuesto.appendChild(getNodo("valor", factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesImpuestos().getDetalleImpuesto().get(j).getValor()));
                elementImpuestos.appendChild(elementImpuesto);
            }

            elementDetalle.appendChild(elementImpuestos);
            elementDetalles.appendChild(elementDetalle);
        }
//alexv pagos
        if(factura.getPagos()!=null){
        Element elementPagos = doc.createElement("pagos"); 
            for(int i = 0; i < factura.getPagos().getPago().size(); i++){
                Element elementPago = doc.createElement("pago");      
                elementPago.appendChild(getNodo("formaPago", factura.getPagos().getPago().get(i).getFormaPago()));
                elementPago.appendChild(getNodo("total", factura.getPagos().getPago().get(i).getTotal()));
                elementPago.appendChild(getNodo("plazo", factura.getPagos().getPago().get(i).getPlazo()));
                elementPago.appendChild(getNodo("unidadTiempo", factura.getPagos().getPago().get(i).getUnidadTiempo()));
                elementPagos.appendChild(elementPago);
            }    
                    infoFactura.appendChild(elementPagos);
        }
//alexv pagos
        for(int i = 0 ; i < comprobanteElectronico.getInfoAdicional().getCamposAdicionales().size(); i++)
        {
            if(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor() != null)
            {
                if(!comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor().equals(""))
                {
                    Element campoAdicionalTmp = getNodo("campoAdicional", comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor());
                    campoAdicionalTmp.setAttribute("nombre",comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getNombre());
                    elementInfoAdicional.appendChild(campoAdicionalTmp);
                }
            }
        }

        elementFactura.appendChild(infoTributaria);
        elementFactura.appendChild(infoFactura);
        elementFactura.appendChild(elementDetalles);
        if(elementInfoAdicional.getChildNodes().getLength() > 0)
        {
            elementFactura.appendChild(elementInfoAdicional);
        }

        doc.appendChild(elementFactura);
        doc.setXmlStandalone(true);
            
        return doc;
    }
    
    
    public Document getRetencionXML(Retencion retencion) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, Exception
    {
        DefaultComprobanteElectronico comprobanteElectronico = (DefaultComprobanteElectronico)retencion; 
        procesadorDeClaveDeAcceso(comprobanteElectronico);
            
        doc = db.newDocument();

        Element elementRetencion = doc.createElement("comprobanteRetencion");
        elementRetencion.setAttribute("id", "comprobante");
        elementRetencion.setAttribute("version", comprobanteElectronico.getDocumentoVersion());

        Element infoTributaria = getInfoTributaria(comprobanteElectronico);
        Element infoCompRetencion = doc.createElement("infoCompRetencion");//getInfoComprobante(comprobanteElectronico,"infoCompRetencion");
        Element elementImpuestos = doc.createElement("impuestos");
        Element elementInfoAdicional = doc.createElement("infoAdicional");

        infoCompRetencion.insertBefore(getNodo("fechaEmision", comprobanteElectronico.getFechaEmision()),infoCompRetencion.getFirstChild());
        appendNodeIfValueIsNotNull("dirEstablecimiento", comprobanteElectronico.getDireccionEstablecimiento(),infoCompRetencion);
        if(!comprobanteElectronico.getContribuyenteEspecial().trim().equals("999"))
        appendNodeIfValueIsNotNull("contribuyenteEspecial", comprobanteElectronico.getContribuyenteEspecial(), infoCompRetencion);
            else
        appendNodeIfValueIsNotNull("contribuyenteEspecial","", infoCompRetencion); 
        appendNodeIfValueIsNotNull("obligadoContabilidad", comprobanteElectronico.getObligadoALlebarContabilidad(), infoCompRetencion); 
        infoCompRetencion.appendChild(getNodo("tipoIdentificacionSujetoRetenido", retencion.getTipoIdentificacionSujetoRetenido()));
        infoCompRetencion.appendChild(getNodo("razonSocialSujetoRetenido", retencion.getRazonSocialSujetoRetenido()));
        infoCompRetencion.appendChild(getNodo("identificacionSujetoRetenido", retencion.getIdentificacionSujetoRetenido()));
        infoCompRetencion.appendChild(getNodo("periodoFiscal", retencion.getPeriodoFiscal()));
       
        for(int i = 0; i < retencion.getImpuestosRetencion().getImpuestoRetencion().size(); i++)
        {
            Element elementImpuesto = doc.createElement("impuesto"); 
            
            elementImpuesto.appendChild(getNodo("codigo",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getCodigo()));
            elementImpuesto.appendChild(getNodo("codigoRetencion",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getCodigoRetencion()));
            elementImpuesto.appendChild(getNodo("baseImponible",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getBaseImponible()));
            elementImpuesto.appendChild(getNodo("porcentajeRetener",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getPorcentajeRetener()));
            elementImpuesto.appendChild(getNodo("valorRetenido",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getValorRetenido()));
            elementImpuesto.appendChild(getNodo("codDocSustento",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getCodDocSustento()));
            appendNodeIfValueIsNotNull("numDocSustento",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getNumDocSustento(), elementImpuesto);
            appendNodeIfValueIsNotNull("fechaEmisionDocSustento",retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getFechaEmisionDocSustento(), elementImpuesto); 
            
            elementImpuestos.appendChild(elementImpuesto);
        }

        for(int i = 0 ; i < comprobanteElectronico.getInfoAdicional().getCamposAdicionales().size(); i++)
        {
            if(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor() != null)
            {
                if(!comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor().equals(""))
                {
                    Element campoAdicionalTmp = getNodo("campoAdicional", comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor());
                    campoAdicionalTmp.setAttribute("nombre",comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getNombre());
                    elementInfoAdicional.appendChild(campoAdicionalTmp);
                }
            }
        }

        elementRetencion.appendChild(infoTributaria);
        elementRetencion.appendChild(infoCompRetencion);
        elementRetencion.appendChild(elementImpuestos);
        
        if(elementInfoAdicional.getChildNodes().getLength() > 0)
        {
            elementRetencion.appendChild(elementInfoAdicional);
        }

        doc.appendChild(elementRetencion);
        doc.setXmlStandalone(true);
            
        return getGenerateDocument();
    }
    
    public Document getRemisionXML(Remision remision) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, Exception
    {
        DefaultComprobanteElectronico comprobanteElectronico = (DefaultComprobanteElectronico)remision;
        procesadorDeClaveDeAcceso(comprobanteElectronico);
            
        doc = db.newDocument();

        Element elementRemision = doc.createElement("guiaRemision");
        elementRemision.setAttribute("id", "comprobante");
        elementRemision.setAttribute("version", comprobanteElectronico.getDocumentoVersion());
        
        Element infoTributaria = getInfoTributaria(comprobanteElectronico);
        Element infoGuiaRemision = doc.createElement("infoGuiaRemision");
        Element elementInfoAdicional = doc.createElement("infoAdicional");
        
        appendNodeIfValueIsNotNull("dirEstablecimiento", comprobanteElectronico.getDireccionEstablecimiento(), infoGuiaRemision);
        infoGuiaRemision.appendChild(getNodo("dirPartida", remision.getDirPartida()));
        infoGuiaRemision.appendChild(getNodo("razonSocialTransportista", remision.getRazonSocialTransportista()));
        infoGuiaRemision.appendChild(getNodo("tipoIdentificacionTransportista", remision.getTipoIdentificacionTransportista()));
        infoGuiaRemision.appendChild(getNodo("rucTransportista", remision.getRucTransportista()));
        appendNodeIfValueIsNotNull("rise", remision.getRise(), infoGuiaRemision);
        appendNodeIfValueIsNotNull("obligadoContabilidad", comprobanteElectronico.getObligadoALlebarContabilidad(), infoGuiaRemision);
        if(!comprobanteElectronico.getContribuyenteEspecial().trim().equals("999"))
        appendNodeIfValueIsNotNull("contribuyenteEspecial", comprobanteElectronico.getContribuyenteEspecial(), infoGuiaRemision);
            else
        appendNodeIfValueIsNotNull("contribuyenteEspecial","", infoGuiaRemision); 
        infoGuiaRemision.appendChild(getNodo("fechaIniTransporte", remision.getFechaIniTransporte()));
        infoGuiaRemision.appendChild(getNodo("fechaFinTransporte", remision.getFechaFinTransporte()));
        infoGuiaRemision.appendChild(getNodo("placa", remision.getPlaca()));
        
        
        Element elementDestinatarios = doc.createElement("destinatarios");
        for(int i = 0; i < remision.getDestinatarios().getDestinatarioRemision().size(); i++)
        {
            Element elementDestinatario = doc.createElement("destinatario");
            
            elementDestinatario.appendChild(getNodo("identificacionDestinatario",remision.getDestinatarios().getDestinatarioRemision().get(i).getIdentificacionDestinatario()));
            elementDestinatario.appendChild(getNodo("razonSocialDestinatario",remision.getDestinatarios().getDestinatarioRemision().get(i).getRazonSocialDestinatario()));
            elementDestinatario.appendChild(getNodo("dirDestinatario",remision.getDestinatarios().getDestinatarioRemision().get(i).getDirDestinatario()));
            elementDestinatario.appendChild(getNodo("motivoTraslado",remision.getDestinatarios().getDestinatarioRemision().get(i).getMotivoTraslado()));
            appendNodeIfValueIsNotNull("docAduaneroUnico", remision.getDestinatarios().getDestinatarioRemision().get(i).getDocAduaneroUnico(), elementDestinatario);  
            appendNodeIfValueIsNotNull("codEstabDestino", remision.getDestinatarios().getDestinatarioRemision().get(i).getCodEstabDestino(), elementDestinatario);
            appendNodeIfValueIsNotNull("ruta", remision.getDestinatarios().getDestinatarioRemision().get(i).getRuta(), elementDestinatario);
            appendNodeIfValueIsNotNull("codDocSustento", remision.getDestinatarios().getDestinatarioRemision().get(i).getCodDocSustento(), elementDestinatario);            
            appendNodeIfValueIsNotNull("numDocSustento", remision.getDestinatarios().getDestinatarioRemision().get(i).getNumDocSustento(), elementDestinatario);
            appendNodeIfValueIsNotNull("numAutDocSustento", remision.getDestinatarios().getDestinatarioRemision().get(i).getNumAutDocSustento(), elementDestinatario);
            appendNodeIfValueIsNotNull("fechaEmisionDocSustento", remision.getDestinatarios().getDestinatarioRemision().get(i).getFechaEmisionDocSustento(), elementDestinatario);
            
            if(remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().size() > 0)
            {
                Element elementDetalles = doc.createElement("detalles");
                for(int j = 0 ; j < remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().size(); j++)
                {
                    Element elementDetalle = getNodo("detalle");
                    elementDetalle.appendChild(getNodo("codigoInterno", remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getCodigoInterno()));
                    appendNodeIfValueIsNotNull("codigoAdicional", remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getCodigoAdicional(), elementDetalle);
                    elementDetalle.appendChild(getNodo("descripcion", remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getDescripcion()));
                    elementDetalle.appendChild(getNodo("cantidad", remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getCantidad()));
                    
                    if(remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getDetallesAdicionales().getDetalleAdicional().size()> 0)
                    {
                        Element elementDetallesAdicionales = doc.createElement("detallesAdicionales");
                        for(int k = 0 ; k < remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getDetallesAdicionales().getDetalleAdicional().size(); k++)
                        {
                            Element detAdicional = getNodo("detAdicional");
                            detAdicional.setAttribute("nombre", remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getDetallesAdicionales().getDetalleAdicional().get(k).getNombre());
                            detAdicional.setAttribute("valor", remision.getDestinatarios().getDestinatarioRemision().get(i).getDetallesRemision().getDetalleRemision().get(j).getDetallesAdicionales().getDetalleAdicional().get(k).getValor());
                            elementDetallesAdicionales.appendChild(detAdicional);
                        }
                        elementDetalle.appendChild(elementDetallesAdicionales);
                    }
                    
                    elementDetalles.appendChild(elementDetalle);
                }
                elementDestinatario.appendChild(elementDetalles);
            }
            elementDestinatarios.appendChild(elementDestinatario);
        }

        for(int i = 0 ; i < comprobanteElectronico.getInfoAdicional().getCamposAdicionales().size(); i++)
        {
            if(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor() != null)
            {
                if(!comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor().equals(""))
                {
                    Element campoAdicionalTmp = getNodo("campoAdicional", comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor());
                    campoAdicionalTmp.setAttribute("nombre",comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getNombre());
                    elementInfoAdicional.appendChild(campoAdicionalTmp);
                }
            }
        }

        elementRemision.appendChild(infoTributaria);
        elementRemision.appendChild(infoGuiaRemision);
        elementRemision.appendChild(elementDestinatarios);
        
        if(elementInfoAdicional.getChildNodes().getLength() > 0)
        {
            elementRemision.appendChild(elementInfoAdicional);
        }

        doc.appendChild(elementRemision);
        doc.setXmlStandalone(true);
            
        return getGenerateDocument();
    }

    public Document getCreditoXML(Credito credito) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, Exception
    {
        DefaultComprobanteElectronico comprobanteElectronico = (DefaultComprobanteElectronico)credito; 
        procesadorDeClaveDeAcceso(comprobanteElectronico);
            
        doc = db.newDocument();

        Element elementFactura = doc.createElement("notaCredito");
        elementFactura.setAttribute("id", "comprobante");
        elementFactura.setAttribute("version", credito.getDocumentoVersion());

        Element infoTributaria = getInfoTributaria(credito);
        Element infoCredito = doc.createElement("infoNotaCredito");//getInfoComprobante(factura,"infoFactura");
        Element elementDetalles = doc.createElement("detalles");
        Element elementInfoAdicional = doc.createElement("infoAdicional");

        infoCredito.appendChild(getNodo("fechaEmision", credito.getFechaEmision()));        
        appendNodeIfValueIsNotNull("dirEstablecimiento", comprobanteElectronico.getDireccionEstablecimiento(), infoCredito);
        infoCredito.appendChild(getNodo("tipoIdentificacionComprador", credito.getTipoIdentificacionDelComprador()));        
        infoCredito.appendChild(getNodo("razonSocialComprador", credito.getRazonSocialDelComprador()));
        infoCredito.appendChild(getNodo("identificacionComprador", credito.getIdentificacionDelComprador()));
        if(!comprobanteElectronico.getContribuyenteEspecial().trim().equals("999"))
        appendNodeIfValueIsNotNull("contribuyenteEspecial", comprobanteElectronico.getContribuyenteEspecial(), infoCredito);
            else
        appendNodeIfValueIsNotNull("contribuyenteEspecial","", infoCredito); 
        appendNodeIfValueIsNotNull("obligadoContabilidad", comprobanteElectronico.getObligadoALlebarContabilidad(), infoCredito);
        appendNodeIfValueIsNotNull("rise", credito.getRise(), infoCredito);
        appendNodeIfValueIsNotNull("codDocModificado", credito.getCodDocModificado(), infoCredito);
        
        infoCredito.appendChild(getNodo("numDocModificado", credito.getNumDocModificado()));
        infoCredito.appendChild(getNodo("fechaEmisionDocSustento", credito.getFechaEmisionDocSustento()));
        infoCredito.appendChild(getNodo("totalSinImpuestos", credito.getTotalSinImpuestos()));
        infoCredito.appendChild(getNodo("valorModificacion", credito.getValorModificacion()));
        appendNodeIfValueIsNotNull("moneda", credito.getMoneda(), infoCredito);
        
        Element elementTotalConImpuestos = doc.createElement("totalConImpuestos");
        for(int i = 0; i < credito.getTotalConImpuestos().getImpuesto().size(); i++)
        {
            Element elementTotalImpuesto = doc.createElement("totalImpuesto");
            elementTotalImpuesto.appendChild(getNodo("codigo",credito.getTotalConImpuestos().getImpuesto().get(i).getCodigo()));
            elementTotalImpuesto.appendChild(getNodo("codigoPorcentaje",credito.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje()));
            elementTotalImpuesto.appendChild(getNodo("baseImponible", credito.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible()));
            //elementTotalImpuesto.appendChild(getNodo("tarifa", credito.getTotalConImpuestos(i).getTarifa()));
            elementTotalImpuesto.appendChild(getNodo("valor", credito.getTotalConImpuestos().getImpuesto().get(i).getValor()));
            elementTotalConImpuestos.appendChild(elementTotalImpuesto);
        }
        infoCredito.appendChild(elementTotalConImpuestos);

        infoCredito.appendChild(getNodo("motivo", credito.getMotivo()));
        
        for(int i = 0; i < credito.getDetallesCredito().getDetalleCredito().size(); i++)
        {
            Element elementDetalle = doc.createElement("detalle");                
            elementDetalle.appendChild(getNodo("codigoInterno",credito.getDetallesCredito().getDetalleCredito().get(i).getCodigoInterno()));
            //appendNodeIfValueIsNotNull("codigoAuxiliar", credito.getDetalle(i).getCodigoAuxiliar(), elementDetalle);
            elementDetalle.appendChild(getNodo("descripcion", credito.getDetallesCredito().getDetalleCredito().get(i).getDescripcion()));
            elementDetalle.appendChild(getNodo("cantidad", credito.getDetallesCredito().getDetalleCredito().get(i).getCantidad()));
            elementDetalle.appendChild(getNodo("precioUnitario", credito.getDetallesCredito().getDetalleCredito().get(i).getPrecioUnitario()));
            elementDetalle.appendChild(getNodo("descuento", credito.getDetallesCredito().getDetalleCredito().get(i).getDescuento()));
            elementDetalle.appendChild(getNodo("precioTotalSinImpuesto", credito.getDetallesCredito().getDetalleCredito().get(i).getPrecioTotalSinImpuesto()));                

            if(credito.getDetallesCredito().getDetalleCredito().get(i).getDetallesAdicionales().getDetalleAdicional().size() > 0)
            {
                Element elementDetallesAdicionales = doc.createElement("detallesAdicionales");
                for(int j = 0 ; j < credito.getDetallesCredito().getDetalleCredito().get(i).getDetallesAdicionales().getDetalleAdicional().size(); j++)
                {
                    Element detAdicional = getNodo("detAdicional");
                    detAdicional.setAttribute("nombre", credito.getDetallesCredito().getDetalleCredito().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getNombre());
                    detAdicional.setAttribute("valor", credito.getDetallesCredito().getDetalleCredito().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getValor());
                    elementDetallesAdicionales.appendChild(detAdicional);
                }
                elementDetalle.appendChild(elementDetallesAdicionales);
            }

            Element elementImpuestos = doc.createElement("impuestos");
            for(int j = 0 ; j < credito.getDetallesCredito().getDetalleCredito().get(i).getImpuestosDetalle().getImpuestoDetalle().size(); j++)
            {
                Element elementImpuesto = doc.createElement("impuesto");
                elementImpuesto.appendChild(getNodo("codigo",credito.getDetallesCredito().getDetalleCredito().get(i).getImpuestosDetalle().getImpuestoDetalle().get(j).getCodigo()));
                elementImpuesto.appendChild(getNodo("codigoPorcentaje", credito.getDetallesCredito().getDetalleCredito().get(i).getImpuestosDetalle().getImpuestoDetalle().get(j).getCodigoPorcentaje()));
                elementImpuesto.appendChild(getNodo("tarifa", credito.getDetallesCredito().getDetalleCredito().get(i).getImpuestosDetalle().getImpuestoDetalle().get(j).getTarifa()));
                elementImpuesto.appendChild(getNodo("baseImponible", credito.getDetallesCredito().getDetalleCredito().get(i).getImpuestosDetalle().getImpuestoDetalle().get(j).getBaseImponible()));
                elementImpuesto.appendChild(getNodo("valor", credito.getDetallesCredito().getDetalleCredito().get(i).getImpuestosDetalle().getImpuestoDetalle().get(j).getValor()));
                elementImpuestos.appendChild(elementImpuesto);
            }

            elementDetalle.appendChild(elementImpuestos);
            elementDetalles.appendChild(elementDetalle);
        }

        for(int i = 0 ; i < comprobanteElectronico.getInfoAdicional().getCamposAdicionales().size(); i++)
        {
            if(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor() != null)
            {
                if(!comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor().equals(""))
                {
                    Element campoAdicionalTmp = getNodo("campoAdicional", comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor());
                    campoAdicionalTmp.setAttribute("nombre",comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getNombre());
                    elementInfoAdicional.appendChild(campoAdicionalTmp);
                }
            }
        }

        elementFactura.appendChild(infoTributaria);
        elementFactura.appendChild(infoCredito);
        elementFactura.appendChild(elementDetalles);
        
        if(elementInfoAdicional.getChildNodes().getLength() > 0)
        {
            elementFactura.appendChild(elementInfoAdicional);
        }

        doc.appendChild(elementFactura);
        doc.setXmlStandalone(true);
            
        return getGenerateDocument();
    }
    
    public Document getDebitoXML(Debito debito) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException, Exception
    {
        DefaultComprobanteElectronico comprobanteElectronico = (DefaultComprobanteElectronico)debito; 
        procesadorDeClaveDeAcceso(comprobanteElectronico);
            
        doc = db.newDocument();

        Element elementFactura = doc.createElement("notaDebito");
        elementFactura.setAttribute("id", "comprobante");
        elementFactura.setAttribute("version", debito.getDocumentoVersion());

        Element infoTributaria = getInfoTributaria(debito);
        Element infoDebito = doc.createElement("infoNotaDebito");//getInfoComprobante(factura,"infoFactura");
        Element elementDetalles = doc.createElement("motivos");
        Element elementInfoAdicional = doc.createElement("infoAdicional");

        infoDebito.appendChild(getNodo("fechaEmision", debito.getFechaEmision()));        
        appendNodeIfValueIsNotNull("dirEstablecimiento", comprobanteElectronico.getDireccionEstablecimiento(), infoDebito);
        infoDebito.appendChild(getNodo("tipoIdentificacionComprador", debito.getTipoIdentificacionDelComprador()));        
        infoDebito.appendChild(getNodo("razonSocialComprador", debito.getRazonSocialDelComprador()));
        infoDebito.appendChild(getNodo("identificacionComprador", debito.getIdentificacionDelComprador()));
        if(!comprobanteElectronico.getContribuyenteEspecial().trim().equals("999"))
        appendNodeIfValueIsNotNull("contribuyenteEspecial", comprobanteElectronico.getContribuyenteEspecial(), infoDebito);
            else
        appendNodeIfValueIsNotNull("contribuyenteEspecial","", infoDebito); 
        appendNodeIfValueIsNotNull("obligadoContabilidad", comprobanteElectronico.getObligadoALlebarContabilidad(), infoDebito);
        appendNodeIfValueIsNotNull("rise", debito.getRise(), infoDebito);
        appendNodeIfValueIsNotNull("codDocModificado", debito.getCodDocModificado(), infoDebito);
        infoDebito.appendChild(getNodo("numDocModificado", debito.getNumDocModificado()));
        infoDebito.appendChild(getNodo("fechaEmisionDocSustento", debito.getFechaEmisionDocSustento()));
        infoDebito.appendChild(getNodo("totalSinImpuestos", debito.getTotalSinImpuestos()));
        
        Element elementTotalConImpuestos = doc.createElement("impuestos");
        for(int i = 0; i < debito.getDebitoImpuestos().getDebitoImpuesto().size(); i++)
        {
            Element elementTotalImpuesto = doc.createElement("impuesto");
            elementTotalImpuesto.appendChild(getNodo("codigo",debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigo()));
            elementTotalImpuesto.appendChild(getNodo("codigoPorcentaje",debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigoPorcentaje()));
            elementTotalImpuesto.appendChild(getNodo("tarifa", debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getTarifa()));
            elementTotalImpuesto.appendChild(getNodo("baseImponible", debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getBaseImponible()));
            elementTotalImpuesto.appendChild(getNodo("valor", debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getValor()));
            elementTotalConImpuestos.appendChild(elementTotalImpuesto);
        }
        infoDebito.appendChild(elementTotalConImpuestos);

        infoDebito.appendChild(getNodo("valorTotal", debito.getValorTotal()));
        
        for(int i = 0; i < debito.getMotivosDebito().getMotivoDebito().size(); i++)
        {
            Element elementDetalle = doc.createElement("motivo");                
            elementDetalle.appendChild(getNodo("razon", debito.getMotivosDebito().getMotivoDebito().get(i).getRazon()));
            elementDetalle.appendChild(getNodo("valor", debito.getMotivosDebito().getMotivoDebito().get(i).getValor()));
            
//            if(debito.getDetalle(i).getDetallesAdicionalesSize() > 0)
//            {
//                Element elementDetallesAdicionales = doc.createElement("detallesAdicionales");
//                for(int j = 0 ; j < debito.getDetalle(i).getDetallesAdicionalesSize(); j++)
//                {
//                    Element detAdicional = getNodo("detAdicional");
//                    detAdicional.setAttribute("nombre", debito.getDetalle(i).getDetalleAdicional(j).getNombre());
//                    detAdicional.setAttribute("valor", debito.getDetalle(i).getDetalleAdicional(j).getValor());
//                    elementDetallesAdicionales.appendChild(detAdicional);
//                }
//                elementDetalle.appendChild(elementDetallesAdicionales);
//            }

//            Element elementImpuestos = doc.createElement("impuestos");
//            for(int j = 0 ; j < debito.getDetalle(i).getDetalleImpuestoSize(); j++)
//            {
//                Element elementImpuesto = doc.createElement("impuesto");
//                elementImpuesto.appendChild(getNodo("codigo",debito.getDetalle(i).getDetalleImpuesto(j).getCodigo()));
//                elementImpuesto.appendChild(getNodo("codigoPorcentaje", debito.getDetalle(i).getDetalleImpuesto(j).getCodigoPorcentaje()));
//                elementImpuesto.appendChild(getNodo("tarifa", debito.getDetalle(i).getDetalleImpuesto(j).getTarifa()));
//                elementImpuesto.appendChild(getNodo("baseImponible", debito.getDetalle(i).getDetalleImpuesto(j).getBaseImponible()));
//                elementImpuesto.appendChild(getNodo("valor", debito.getDetalle(i).getDetalleImpuesto(j).getValor()));
//                elementImpuestos.appendChild(elementImpuesto);
//            }
//
//            elementDetalle.appendChild(elementImpuestos);
            elementDetalles.appendChild(elementDetalle);
        }

        for(int i = 0 ; i < comprobanteElectronico.getInfoAdicional().getCamposAdicionales().size(); i++)
        {
            if(comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor() != null)
            {
                if(!comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor().equals(""))
                {
                    Element campoAdicionalTmp = getNodo("campoAdicional", comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getValor());
                    campoAdicionalTmp.setAttribute("nombre",comprobanteElectronico.getInfoAdicional().getCamposAdicionales().get(i).getNombre());
                    elementInfoAdicional.appendChild(campoAdicionalTmp);
                }
            }
        }

        elementFactura.appendChild(infoTributaria);
        elementFactura.appendChild(infoDebito);
        elementFactura.appendChild(elementDetalles);
        
        if(elementInfoAdicional.getChildNodes().getLength() > 0)
        {
            elementFactura.appendChild(elementInfoAdicional);
        }

        doc.appendChild(elementFactura);
        doc.setXmlStandalone(true);
            
        return getGenerateDocument();
    }
}