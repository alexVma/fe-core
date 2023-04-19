/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.portal.webService.soapObject;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class DefaultBaseComprobante 
{
    private String codigoRandomico = null;
    
    private String codigoAmbiente = null;
    private String codigoTipoEmision = null;
    private String ruc = null;
    private String codigoDocumento = null;
    private String establecimiento = null;
    private String puntoEmision = null;
    private String secuencial = null;    
    
    private String fechaEmision = null;

    private String claveDeAcceso = null;
    private String numeroDeAutorizacion = null;
    
    public DefaultBaseComprobante() {
    }
    
    public DefaultBaseComprobante(String establecimiento, String puntoEmision, String secuencial,String fechaEmision, String claveDeAcceso,String numeroDeAutorizacion) 
    {
        this.establecimiento = establecimiento;
        this.puntoEmision = puntoEmision;
        this.secuencial = secuencial;
        this.fechaEmision = fechaEmision;
        this.claveDeAcceso = claveDeAcceso;
        this.numeroDeAutorizacion = numeroDeAutorizacion;
    }

    public void setClaveDeAcceso(String claveDeAcceso) {
        this.claveDeAcceso = claveDeAcceso;
    }

    public String getClaveDeAcceso() {
        return claveDeAcceso;
    }

    public void setNumeroDeAutorizacion(String numeroDeAutorizacion) {
        this.numeroDeAutorizacion = numeroDeAutorizacion;
    }

    public String getNumeroDeAutorizacion() {
        return numeroDeAutorizacion;
    }
    
    public void setCodigoRandomico(String codigoRandomico) {
        this.codigoRandomico = codigoRandomico;
    }

    @XmlElement(required=true)
    public String getCodigoRandomico() {
        return codigoRandomico;
    }    
    
    public void setCodigoAmbiente(String codigoAmbiente)
    {
        this.codigoAmbiente = codigoAmbiente;
    }

    @XmlElement(required=true)
    public String getCodigoAmbiente() {
        return codigoAmbiente;
    }
    
    public void setCodigoTipoEmision(String codigoTipoEmision)
    {
        this.codigoTipoEmision = codigoTipoEmision;
    }

    @XmlElement(required=true)
    public String getCodigoTipoEmision() {
        return codigoTipoEmision;
    }   
    
    public void setRuc(String ruc)
    {
        this.ruc = ruc;
    }

    @XmlElement(required=true)
    public String getRuc() {
        return ruc;
    }
    
    public void setCodigoDocumento(String codigoDocumento)
    {
        this.codigoDocumento = codigoDocumento;
    }

    @XmlElement(required=true)
    public String getCodigoDocumento() {
        return codigoDocumento;
    }
    
    public void setEstablecimiento(String establecimiento)
    {
        this.establecimiento = establecimiento;
    }

    @XmlElement(required=true)
    public String getEstablecimiento() {
        return establecimiento;
    }
    
    public void setPuntoEmision(String puntoEmision)
    {
        this.puntoEmision = puntoEmision;
    }

    @XmlElement(required=true)
    public String getPuntoEmision() {
        return puntoEmision;
    }
    
    public void setSecuencial(String secuencial)
    {
        this.secuencial = secuencial;
    }

    @XmlElement(required=true)
    public String getSecuencial() {
        return secuencial;
    }    
    
    public void setFechaEmision(String fechaEmision)
    {
        this.fechaEmision = fechaEmision;
    }

    @XmlElement(required=true)
    public String getFechaEmision() {
        return fechaEmision;
    }   
    
    public String generarClaveDeAccesoNormal() throws Exception
    {
        if(codigoRandomico != null)
        {
            if(codigoRandomico.trim().equals(""))
            {
                codigoRandomico = generarRandom();
            }
        }
        else
        {
            codigoRandomico = generarRandom();
        }
        
        String claveAccesoTmp = fechaEmision.replaceAll("/", "")+codigoDocumento+ruc+codigoAmbiente+establecimiento+puntoEmision+secuencial+codigoRandomico+codigoTipoEmision;
        int modulo11 = org.inprofsolutions.utils.checkDigit.Module11.digitoVerificador(claveAccesoTmp);
        String claveAccesoFinal = claveAccesoTmp+modulo11;

        return claveAccesoFinal;
    }
    
    public String generarClaveDeAccesoContigente(String numContigencia) throws Exception
    {
        String claveAccesoTmp = fechaEmision.replaceAll("/", "")+codigoDocumento+numContigencia+codigoTipoEmision;
        int modulo11 = org.inprofsolutions.utils.checkDigit.Module11.digitoVerificador(claveAccesoTmp);
        return claveAccesoTmp+modulo11;
    }
    
    private String generarRandom()
    {
        int codigoNumericoInt = new Double(Math.random()*100000000).intValue();
        if (codigoNumericoInt == 0)
        {
            codigoNumericoInt = 1;
        }
        String codigoNumericoString = "00000000"+codigoNumericoInt;
        codigoNumericoString = codigoNumericoString.substring(codigoNumericoString.length()-8);
        return codigoNumericoString;
    }
}
