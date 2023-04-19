/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.detalles;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DetallesAdicionales;

/**
 *
 * @author caf
 */
public class DetalleRemision 
{
    private String codigoInterno = null;
    private String codigoAdicional = null;
    private String descripcion = null;
    private String cantidad = null;
    
    //private ArrayList detallesAdicionales = new ArrayList();
    private DetallesAdicionales detallesAdicionales= null;
    
    public DetalleRemision() {
    }
    
    public DetalleRemision(String codigoInterno, String codigoAdicional, String descripcion, String cantidad) {
        setCodigoInterno(codigoInterno);
        setCodigoAdicional(codigoAdicional);
        setDescripcion(descripcion);
        setCantidad(cantidad);
    }
    
    
    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    @XmlElement(required=true)
    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoAdicional(String codigoAdicional) {
        this.codigoAdicional = codigoAdicional;
    }

    public String getCodigoAdicional() {
        return codigoAdicional;
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
}