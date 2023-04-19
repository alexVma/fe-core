/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class DefaultComprobanteElectronico extends DefaultBaseComprobante
{
    private String documentoVersion = null;
    
    private String razonSocial = null;
    private String nombreComercial = null;
    private String claveAcceso = null;
    private String direccionMatriz =null;
    
    private String direccionEstablecimiento = null;
    private String contribuyenteEspecial = null;
    private String obligadoALlebarContabilidad = null;
    
    //private ArrayList infoAdicional = new ArrayList();
    private InfoAdicional infoAdicional = new InfoAdicional();


    
    public void setDocumentoVersion(String version)
    {
        this.documentoVersion = version;
    }

    @XmlElement(required=true)
    public String getDocumentoVersion() {
        return documentoVersion;
    }
    

    
    public void setRazonSocial(String razonSocial)
    {
        this.razonSocial = razonSocial;
    }

    @XmlElement(required=true)
    public String getRazonSocial() {
        return razonSocial;
    }
    
    public void setNombreComercial(String nombreComercial)
    {
        this.nombreComercial = nombreComercial;
    }

    @XmlElement(required=true)
    public String getNombreComercial() {
        return nombreComercial;
    }
    
    public void setClaveAcceso(String claveAcceso)
    {
        this.claveAcceso = claveAcceso;
    }
    
    public String getClaveAcceso()
    {
        return this.claveAcceso;
    }
    

   
    public void setDireccionMatriz(String direccionMatriz)
    {
        this.direccionMatriz = direccionMatriz;
    } 

    @XmlElement(required=true)
    public String getDireccionMatriz() {
        return direccionMatriz;
    }
    
    public void setDireccionEstablecimiento(String direccionEstablecimiento)
    {
        this.direccionEstablecimiento = direccionEstablecimiento;
    }

    @XmlElement(required=true)
    public String getDireccionEstablecimiento() {
        return direccionEstablecimiento;
    }
    
    public void setContribuyenteEspecial(String contribuyenteEspecial)
    {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public String getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }
    
    public void setObligadoALlebarContabilidad(String obligadoALlebarContabilidad)
    {
        this.obligadoALlebarContabilidad = obligadoALlebarContabilidad;
    }

    public String getObligadoALlebarContabilidad() {
        return obligadoALlebarContabilidad;
    }
    
//    public void addCampoAdicional(CampoAdicional campoAdicional)
//    {
//        infoAdicional.add(campoAdicional);
//    }
//    
//    public int getCampoAdicionalSize()
//    {
//        return infoAdicional.size();
//    }
//    
//    public CampoAdicional getCampoAdicional(int index)
//    {
//        return (CampoAdicional)infoAdicional.get(index);
//    }
    
    public void setInfoAdicional(InfoAdicional infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    @XmlElement(required=true)
    public InfoAdicional getInfoAdicional() {
        return infoAdicional;
    }        
}
