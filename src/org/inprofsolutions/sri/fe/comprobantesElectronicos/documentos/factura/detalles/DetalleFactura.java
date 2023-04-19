/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DetallesAdicionales;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.impuestos.DetallesImpuestos;

/**
 *
 * @author caf
 */
public class DetalleFactura 
{
    private String codigoPrincipal = null; 
    private String codigoAuxiliar = null;
    private String descripcion = null;
    private String cantidad = null;
    private String precioUnitario = null;
    private String descuento = null;
    private String precioTotalSinImpuesto = null;
    private String precioSinSubsidio = null;
    private DetallesAdicionales detallesAdicionales = null;
    private DetallesImpuestos detallesImpuestos = null;

    public DetalleFactura() 
    {
    }

    public DetalleFactura(String codigoPrincipal, String descripcion, String cantidad, String precioUnitario, String descuento, String precioTotalSinImpuesto) 
    {
        setCodigoPrincipal(codigoPrincipal);
        setDescripcion(descripcion);
        setCantidad(cantidad);
        setPrecioUnitario(precioUnitario);
        setDescuento(descuento);
        setPrecioTotalSinImpuesto(precioTotalSinImpuesto);
    }
    
    public void setDetallesAdicionales(DetallesAdicionales detallesAdicionales) {
        this.detallesAdicionales = detallesAdicionales;
    }

    public DetallesAdicionales getDetallesAdicionales() {
        if (detallesAdicionales == null) 
        {
            detallesAdicionales = new DetallesAdicionales();
        }
        return detallesAdicionales;
    }

    public void setDetallesImpuestos(DetallesImpuestos detallesImpuestos) {
        this.detallesImpuestos = detallesImpuestos;
    }

    @XmlElement(required=true)
    public DetallesImpuestos getDetallesImpuestos() {
        if (detallesImpuestos == null) 
        {
            detallesImpuestos = new DetallesImpuestos();
        }
        return detallesImpuestos;
    }

    public void setCodigoPrincipal(String codigoPrincipal) {
        this.codigoPrincipal = codigoPrincipal;
    }

    @XmlElement(required=true)
    public String getCodigoPrincipal() {
        return codigoPrincipal;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlElement(required=true)
    public String getDescripcion() {
        return descripcion;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    @XmlElement(required=true)
    public String getCantidad() {
        return cantidad;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @XmlElement(required=true)
    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    @XmlElement(required=true)
    public String getDescuento() {
        return descuento;
    }

    public void setPrecioTotalSinImpuesto(String precioTotalSinImpuesto) {
        this.precioTotalSinImpuesto = precioTotalSinImpuesto;
    }

    @XmlElement(required=true)
    public String getPrecioTotalSinImpuesto() {
        return precioTotalSinImpuesto;
    }
    
    public String getPrecioSinSubsidio() {
        return precioSinSubsidio;
    }

    public void setPrecioSinSubsidio(String precioSinSubsidio) {
        this.precioSinSubsidio = precioSinSubsidio;
    }
    
}