/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.pagos;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DetallesAdicionales;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.impuestos.DetallesImpuestos;

/**
 *
 * @author caf
 */
public class PagoFactura 
{
    private String formaPago = null; 
    private String total = null;
    private String plazo = null;
    private String unidadTiempo = null;


//    public PagoFactura() 
//    {
//    }
//
//    public PagoFactura(String codigoPrincipal, String descripcion, String cantidad, String precioUnitario, String descuento, String precioTotalSinImpuesto) 
//    {
//        setCodigoPrincipal(codigoPrincipal);
//        setDescripcion(descripcion);
//        setCantidad(cantidad);
//        setPrecioUnitario(precioUnitario);
//        setDescuento(descuento);
//        setPrecioTotalSinImpuesto(precioTotalSinImpuesto);
//    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public void setUnidadTiempo(String unidadTiempo) {
        this.unidadTiempo = unidadTiempo;
    }

    @XmlElement(required=true)
    public String getFormaPago() {
        return formaPago;
    }

    @XmlElement(required=true)
    public String getTotal() {
        return total;
    }

    @XmlElement(required=true)
    public String getPlazo() {
        return plazo;
    }

    @XmlElement(required=true)
    public String getUnidadTiempo() {
        return unidadTiempo;
    }
    
    
    
}