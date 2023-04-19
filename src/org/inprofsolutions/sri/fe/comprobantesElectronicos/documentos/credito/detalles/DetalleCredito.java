/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.impuestos.ImpuestosDetalle;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DetallesAdicionales;

/**
 *
 * @author caf
 */
public class DetalleCredito 
{
    private String codigoInterno = null; 
    private String descripcion = null;
    private String cantidad = null;
    private String precioUnitario = null;
    private String descuento = null;
    private String precioTotalSinImpuesto = null;
    
    //private ArrayList detallesAdicionales = new ArrayList();
    private DetallesAdicionales detallesAdicionales= null;
    
    //private ArrayList impuestos = new ArrayList();
    private ImpuestosDetalle impuestosDetalle = null;
    
    public DetalleCredito() {
    }

    public DetalleCredito(String codigoInterno, String descripcion, String cantidad, String precioUnitario, String descuento, String precioTotalSinImpuesto) 
    {
        setCodigoInterno(codigoInterno);
        setDescripcion(descripcion);
        setCantidad(cantidad);
        setPrecioUnitario(precioUnitario);
        setDescuento(descuento);
        setPrecioTotalSinImpuesto(precioTotalSinImpuesto);
    }
    
//    public DetalleCredito(String codigoInterno, String descripcion, String cantidad, String precioUnitario, String descuento, String precioTotalSinImpuesto, Impuesto impuesto) 
//    {
//        setCodigoInterno(codigoInterno);
//        setDescripcion(descripcion);
//        setCantidad(cantidad);
//        setPrecioUnitario(precioUnitario);
//        setDescuento(descuento);
//        setPrecioTotalSinImpuesto(precioTotalSinImpuesto);
//        addDetalleImpuesto(impuesto);
//    }
    
//    public void addDetalleAdicional(DetalleAdicional detalleAdicional)
//    {
//        detallesAdicionales.add(detalleAdicional);
//    }
//    
//    public int getDetallesAdicionalesSize()
//    {
//        return detallesAdicionales.size();
//    }
//    
//    public DetalleAdicional getDetalleAdicional(int index)
//    {
//        return (DetalleAdicional)detallesAdicionales.get(index);
//    }
    
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
    
//    public void addDetalleImpuesto(Impuesto detalleImpuesto)
//    {
//        impuestos.add(detalleImpuesto);
//    }
//    
//    public int getDetalleImpuestoSize()
//    {
//        return impuestos.size();
//    }
//    
//    public Impuesto getDetalleImpuesto(int index)
//    {
//        return (Impuesto)impuestos.get(index);
//    }
    public void setImpuestosDetalle(ImpuestosDetalle impuestosDetalle) {
        this.impuestosDetalle = impuestosDetalle;
    }

    @XmlElement(required=true)
    public ImpuestosDetalle getImpuestosDetalle() {
        if (impuestosDetalle == null) 
        {
            impuestosDetalle = new ImpuestosDetalle();
        }
        return impuestosDetalle;
    }
    
    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    @XmlElement(required=true)
    public String getCodigoInterno() {
        return codigoInterno;
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
}