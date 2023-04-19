/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.TotalConImpuestos;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetallesFactura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.datosAdicionales.AguaDatoAdicional;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.pagos.Pagos;

/**
 *
 * @author caf
 */
public class Factura extends DefaultComprobanteElectronico
{
    //infoFactura

    private String tipoIdentificacionDelComprador = null;
    private String guiaRemision = null;
    private String razonSocialDelComprador = null;
    private String identificacionDelComprador = null;
    private String totalSinImpuestos = null;
    private String totalDescuento = null;   
    private String totalSubsidio = null;
    private TotalConImpuestos totalConImpuestos = null;
    private String propina = null;
    private String importeTotal = null;
    private String moneda = null;
    private DetallesFactura detallesFactura = null;
    private Pagos pagos=null;
    private AguaDatoAdicional aguaDatoAdicional=null;
            
    public void setTipoIdentificacionDelComprador(String tipoIdentificacionDelComprador)
    {
        this.tipoIdentificacionDelComprador = tipoIdentificacionDelComprador;        
    }

    @XmlElement(required=true)
    public String getTipoIdentificacionDelComprador() {
        return tipoIdentificacionDelComprador;
    }
    
    public void setGuiaRemision(String guiaRemision)
    {
        this.guiaRemision = guiaRemision;
    }

    public String getGuiaRemision() {
        return guiaRemision;
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
    
    public void setTotalSinImpuestos(String totalSinImpuestos)
    {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    @XmlElement(required=true)
    public String getTotalSinImpuestos() {
        return totalSinImpuestos;
    }
    
    public void setTotalDescuento(String totalDescuento)
    {
        this.totalDescuento = totalDescuento;
    }

    @XmlElement(required=true)
    public String getTotalDescuento() {
        return totalDescuento;
    }
   
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
    
    public void setPropina(String propina)
    {
        this.propina = propina;
    }

    @XmlElement(required=true)
    public String getPropina() {
        return propina;
    }

    public void setImporteTotal(String importeTotal)
    {
        this.importeTotal = importeTotal;
    }

    @XmlElement(required=true)
    public String getImporteTotal() {
        return importeTotal;
    }
    
    public void setMoneda(String moneda)
    {
        this.moneda = moneda;
    }

    @XmlElement(required=true)
    public String getMoneda() {
        return moneda;
    }
    
    public void setDetallesFactura(DetallesFactura detallesFactura) {
        this.detallesFactura = detallesFactura;
    }

    @XmlElement(required=true)
    public DetallesFactura getDetallesFactura() {
        if (detallesFactura == null) 
        {
            detallesFactura = new DetallesFactura();
        }
        return detallesFactura;
    }

    public String getTotalSubsidio() {
        return totalSubsidio;
    }

    public void setTotalSubsidio(String totalSubsidio) {
        this.totalSubsidio = totalSubsidio;
    }

    public Pagos getPagos() {
        return pagos;
    }

    public void setPagos(Pagos pagos) {
        this.pagos = pagos;
    }

    public AguaDatoAdicional getAguaDatoAdicional() {
        return aguaDatoAdicional;
    }

    public void setAguaDatoAdicional(AguaDatoAdicional aguaDatoAdicional) {
        this.aguaDatoAdicional = aguaDatoAdicional;
    }
    
}