/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos.MotivosDebito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos.impuestos.DebitoImpuestos;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;

/**
 *
 * @author caf
 */
public class Debito extends DefaultComprobanteElectronico
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
    
    //private ArrayList impuestos = new ArrayList();
    private DebitoImpuestos debitoImpuestos = null;
    
    private String valorTotal = null;
    
    //private ArrayList motivos = new ArrayList();
    private MotivosDebito motivosDebito = null;
    
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

//    private void addImpuestos(Impuesto impuesto)
//    {
//        impuestos.add(impuesto);
//    }
//    
//    private void removeImpuestos(Impuesto impuesto)
//    {
//        impuestos.remove(impuesto);
//    }
//    
//    public int getImpuestosSize()
//    {
//        return impuestos.size();
//    }
//    
//    public Impuesto getImpuestos(int index)
//    {
//        return (Impuesto)impuestos.get(index);
//    }
    
    public void setDebitoImpuestos(DebitoImpuestos debitoImpuestos) {
        this.debitoImpuestos = debitoImpuestos;
    }

    @XmlElement(required=true)
    public DebitoImpuestos getDebitoImpuestos() {
        if (debitoImpuestos == null) 
        {
            debitoImpuestos = new DebitoImpuestos();
        }
        return debitoImpuestos;
    }

    
    
    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    @XmlElement(required=true)
    public String getValorTotal() {
        return valorTotal;
    }

//    public int getMotivosSize()
//    {
//        return motivos.size();
//    }
//    
//    public MotivoDebito getMotivo(int index)
//    {
//        return (MotivoDebito)motivos.get(index);
//    }
    
    public void setMotivosDebito(MotivosDebito motivosDebito) {
        this.motivosDebito = motivosDebito;
    }

    @XmlElement(required=true)
    public MotivosDebito getMotivosDebito() {
        if (motivosDebito == null) 
        {
            motivosDebito = new MotivosDebito();
        }
        return motivosDebito;
    }
}