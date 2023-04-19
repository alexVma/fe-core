/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.pagos.PagosFactura;

/**
 *
 * @author caf
 */
public class FacturaExportacion extends Factura
{
    private String comercioExterior = null;
    private String IncoTermFactura = null;
    private String lugarIncoTerm = null;
    private String paisOrigen = null;
    private String puertoEmbarque = null;
    private String puertoDestino = null;
    private String paisDestino = null;
    private String paisAdquisicion = null;
    private String incoTermTotalSinImpuestos = null;
    
    private String fleteInternacional = null;
    private String seguroInternacional = null;
    private String gastosAduaneros = null;
    private String gastosTransporteOtros = null;
    
    private PagosFactura pagosFactura = null;
    
    public void setComercioExterior(String comercioExterior) {
        this.comercioExterior = comercioExterior;
    }

    public void setIncoTermFactura(String IncoTermFactura) {
        this.IncoTermFactura = IncoTermFactura;
    }

    public void setLugarIncoTerm(String lugarIncoTerm) {
        this.lugarIncoTerm = lugarIncoTerm;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public void setPuertoEmbarque(String puertoEmbarque) {
        this.puertoEmbarque = puertoEmbarque;
    }

    public void setPuertoDestino(String puertoDestino) {
        this.puertoDestino = puertoDestino;
    }

    public void setPaisDestino(String paisDestino) {
        this.paisDestino = paisDestino;
    }

    public void setPaisAdquisicion(String paisAdquisicion) {
        this.paisAdquisicion = paisAdquisicion;
    }

    public void setIncoTermTotalSinImpuestos(String incoTermTotalSinImpuestos) {
        this.incoTermTotalSinImpuestos = incoTermTotalSinImpuestos;
    }

    public void setFleteInternacional(String fleteInternacional) {
        this.fleteInternacional = fleteInternacional;
    }

    public void setSeguroInternacional(String seguroInternacional) {
        this.seguroInternacional = seguroInternacional;
    }

    public void setGastosAduaneros(String gastosAduaneros) {
        this.gastosAduaneros = gastosAduaneros;
    }

    public void setGastosTransporteOtros(String gastosTransporteOtros) {
        this.gastosTransporteOtros = gastosTransporteOtros;
    }
    
    public void setPagosFactura(PagosFactura pagosFactura) {
        this.pagosFactura = pagosFactura;
    }
    
    @XmlElement(required=true)
    public String getComercioExterior() {
        return comercioExterior;
    }

    @XmlElement(required=true)
    public String getIncoTermFactura() {
        return IncoTermFactura;
    }

    @XmlElement(required=true)
    public String getLugarIncoTerm() {
        return lugarIncoTerm;
    }

    @XmlElement(required=true)
    public String getPaisOrigen() {
        return paisOrigen;
    }

    @XmlElement(required=true)
    public String getPuertoEmbarque() {
        return puertoEmbarque;
    }

    @XmlElement(required=true)
    public String getPuertoDestino() {
        return puertoDestino;
    }

    public String getPaisDestino() {
        return paisDestino;
    }

    public String getPaisAdquisicion() {
        return paisAdquisicion;
    }

    public String getIncoTermTotalSinImpuestos() {
        return incoTermTotalSinImpuestos;
    }

    public String getFleteInternacional() {
        return fleteInternacional;
    }

    public String getSeguroInternacional() {
        return seguroInternacional;
    }

    public String getGastosAduaneros() {
        return gastosAduaneros;
    }

    public String getGastosTransporteOtros() {
        return gastosTransporteOtros;
    }

    public PagosFactura getPagosFactura() {
        if (pagosFactura == null) 
        {
            pagosFactura = new PagosFactura();
        }
        return pagosFactura;
    }
}
