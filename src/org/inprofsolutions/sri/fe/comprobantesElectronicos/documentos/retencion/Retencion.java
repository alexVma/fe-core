/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.impuestos.ImpuestosRetencion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;


/**
 *
 * @author caf
 */
public class Retencion extends DefaultComprobanteElectronico
{
    //infoFactura

    private String tipoIdentificacionSujetoRetenido = null;
    private String razonSocialSujetoRetenido = null;
    private String identificacionSujetoRetenido = null;
    private String periodoFiscal = null;
    
    //private ArrayList impuestos = new ArrayList();
    private ImpuestosRetencion impuestosRetencion = null;

    public void setTipoIdentificacionSujetoRetenido(String tipoIdentificacionSujetoRetenido) {
        this.tipoIdentificacionSujetoRetenido = tipoIdentificacionSujetoRetenido;
    }

    @XmlElement(required=true)
    public String getTipoIdentificacionSujetoRetenido() {
        return tipoIdentificacionSujetoRetenido;
    }

    public void setRazonSocialSujetoRetenido(String razonSocialSujetoRetenido) {
        this.razonSocialSujetoRetenido = razonSocialSujetoRetenido;
    }

    @XmlElement(required=true)
    public String getRazonSocialSujetoRetenido() {
        return razonSocialSujetoRetenido;
    }

    public void setIdentificacionSujetoRetenido(String identificacionSujetoRetenido) {
        this.identificacionSujetoRetenido = identificacionSujetoRetenido;
    }

    @XmlElement(required=true)
    public String getIdentificacionSujetoRetenido() {
        return identificacionSujetoRetenido;
    }

    public void setPeriodoFiscal(String periodoFiscal) {
        this.periodoFiscal = periodoFiscal;
    }

    @XmlElement(required=true)
    public String getPeriodoFiscal() {
        return periodoFiscal;
    }
    
//    public void addImpuesto(ImpuestoRetencion Impuesto)
//    {
//        impuestos.add(Impuesto);
//    }
//    
//    public int getImpuestoSize()
//    {
//        return impuestos.size();
//    }
//    
//    public ImpuestoRetencion getImpuesto(int index)
//    {
//        return (ImpuestoRetencion)impuestos.get(index);
//    }
    
    public void setImpuestosRetencion(ImpuestosRetencion impuestosRetencion) {
        this.impuestosRetencion = impuestosRetencion;
    }

    @XmlElement(required=true)
    public ImpuestosRetencion getImpuestosRetencion() 
    {
        if (impuestosRetencion == null) 
        {
            impuestosRetencion = new ImpuestosRetencion();
        }
        return impuestosRetencion;
    }
    
}