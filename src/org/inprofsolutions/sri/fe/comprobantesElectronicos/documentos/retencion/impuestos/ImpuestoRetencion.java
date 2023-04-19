/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.impuestos;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class ImpuestoRetencion 
{
    private String codigo = null;
    private String codigoRetencion = null;
    private String baseImponible = null;
    private String porcentajeRetener = null;
    private String valorRetenido = null;
    private String codDocSustento = null;
    private String numDocSustento = null;
    private String fechaEmisionDocSustento = null;

    public ImpuestoRetencion() {
    }
    
    public ImpuestoRetencion(String codigo, String codigoRetencion, String baseImponible, String porcentajeRetener, String valorRetenido, String codDocSustento, String numDocSustento, String fechaEmisionDocSustento) 
    {
        setCodigo(codigo);
        setCodigoRetencion(codigoRetencion);
        setBaseImponible(baseImponible);
        setPorcentajeRetener(porcentajeRetener);
        setValorRetenido(valorRetenido);
        setCodDocSustento(codDocSustento);
        setNumDocSustento(numDocSustento);
        setFechaEmisionDocSustento(fechaEmisionDocSustento);
    }

    
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @XmlElement(required=true)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigoRetencion(String codigoRetencion) {
        this.codigoRetencion = codigoRetencion;
    }

    @XmlElement(required=true)
    public String getCodigoRetencion() {
        return codigoRetencion;
    }

    public void setBaseImponible(String baseImponible) {
        this.baseImponible = baseImponible;
    }

    @XmlElement(required=true)
    public String getBaseImponible() {
        return baseImponible;
    }

    public void setPorcentajeRetener(String porcentajeRetener) {
        this.porcentajeRetener = porcentajeRetener;
    }

    @XmlElement(required=true)
    public String getPorcentajeRetener() {
        return porcentajeRetener;
    }

    public void setValorRetenido(String valorRetenido) {
        this.valorRetenido = valorRetenido;
    }

    @XmlElement(required=true)
    public String getValorRetenido() {
        return valorRetenido;
    }

    public void setCodDocSustento(String codDocSustento) {
        this.codDocSustento = codDocSustento;
    }

    @XmlElement(required=true)
    public String getCodDocSustento() {
        return codDocSustento;
    }

    public void setNumDocSustento(String numDocSustento) {
        this.numDocSustento = numDocSustento;
    }

    @XmlElement(required=true)
    public String getNumDocSustento() {
        return numDocSustento;
    }

    public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    @XmlElement(required=true)
    public String getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }
}
