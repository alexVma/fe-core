/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.DetallesCredito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.TotalConImpuestos;

/**
 *
 * @author caf
 */
public class Credito extends DefaultComprobanteElectronico
{
    //infoCredito

    private String tipoIdentificacionDelComprador = null;
    private String razonSocialDelComprador = null;
    private String identificacionDelComprador = null;
    
    private String rise = null;
    private String codDocModificado = null;
    private String numDocModificado = null;
    private String fechaEmisionDocSustento = null;
    
    private String totalSinImpuestos = null;
    
    private String valorModificacion = null;
    
    private String moneda = null;
    
    //private ArrayList totalConImpuestos = new ArrayList();   
    private TotalConImpuestos totalConImpuestos = new TotalConImpuestos();
    
    private String motivo = null;
    
    //private ArrayList detalles = new ArrayList();
    private DetallesCredito detallesCredito = null;
    
    public void setTipoIdentificacionDelComprador(String tipoIdentificacionDelComprador)
    {
        this.tipoIdentificacionDelComprador = tipoIdentificacionDelComprador;        
    }

    @XmlElement(required=true)
    public String getTipoIdentificacionDelComprador() {
        return tipoIdentificacionDelComprador;
    }
    
    public void setRazonSocialDelComprador(String razonSocialDelComprador)
    {
        this.razonSocialDelComprador = razonSocialDelComprador;
    }

    @XmlElement(required=true)
    public String getRazonSocialDelComprador() {
        return razonSocialDelComprador;
    }
    
    public void setIdentificacionDelComprador(String identificacionDelComprador)
    {
        this.identificacionDelComprador = identificacionDelComprador;
    }

    @XmlElement(required=true)
    public String getIdentificacionDelComprador() {
        return identificacionDelComprador;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getRise() {
        return rise;
    }

    public void setCodDocModificado(String codDocModificado) {
        this.codDocModificado = codDocModificado;
    }

    @XmlElement(required=true)
    public String getCodDocModificado() {
        return codDocModificado;
    }

    public void setNumDocModificado(String numDocModificado) {
        this.numDocModificado = numDocModificado;
    }

    @XmlElement(required=true)
    public String getNumDocModificado() {
        return numDocModificado;
    }

    public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    @XmlElement(required=true)
    public String getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }
    
    public void setTotalSinImpuestos(String totalSinImpuestos)
    {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    @XmlElement(required=true)
    public String getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setValorModificacion(String valorModificacion) {
        this.valorModificacion = valorModificacion;
    }

    @XmlElement(required=true)
    public String getValorModificacion() {
        return valorModificacion;
    }
    
//    private void addTotalConImpuestos(Impuesto impuesto)
//    {
//        totalConImpuestos.add(impuesto);
//    }
//    
//    private void removeTotalConImpuestos(Impuesto impuesto)
//    {
//        totalConImpuestos.remove(impuesto);
//    }
//    
//    public int getTotalConImpuestosSize()
//    {
//        return totalConImpuestos.size();
//    }
//    
//    public Impuesto getTotalConImpuestos(int index)
//    {
//        return (Impuesto)totalConImpuestos.get(index);
//    }

    public void setTotalConImpuestos(TotalConImpuestos totalConImpuestos) {
        this.totalConImpuestos = totalConImpuestos;
    }

    @XmlElement(required=true)
    public TotalConImpuestos getTotalConImpuestos() {
        if (totalConImpuestos == null) 
        {
            totalConImpuestos = new TotalConImpuestos();
        }
        return totalConImpuestos;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @XmlElement(required=true)
    public String getMotivo() {
        return motivo;
    }
    
    public void setMoneda(String moneda)
    {
        this.moneda = moneda;
    }

    @XmlElement(required=true)
    public String getMoneda() {
        return moneda;
    }
    
//    public int getDetallesSize()
//    {
//        return detalles.size();
//    }
//    
//    public DetalleCredito getDetalle(int index)
//    {
//        return (DetalleCredito)detalles.get(index);
//    }
    
    public void setDetallesCredito(DetallesCredito detallesCredito) {
        this.detallesCredito = detallesCredito;
    }

    @XmlElement(required=true)
    public DetallesCredito getDetallesCredito() {
        if (detallesCredito == null) 
        {
            detallesCredito = new DetallesCredito();
        }
        return detallesCredito;
    }
}