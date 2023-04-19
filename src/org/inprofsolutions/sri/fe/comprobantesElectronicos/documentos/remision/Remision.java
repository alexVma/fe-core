/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.DestinatariosRemision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;

/**
 *
 * @author caf
 */
public class Remision extends DefaultComprobanteElectronico
{
    private String dirPartida = null;
    private String razonSocialTransportista = null;    
    private String tipoIdentificacionTransportista = null;    
    private String rucTransportista = null;
    private String rise = null;
    private String fechaIniTransporte = null;
    private String fechaFinTransporte = null;
    private String placa = null;
    
    //private ArrayList destinatarios = new ArrayList();
    private DestinatariosRemision destinatarios = null;
    
    public void setDirPartida(String dirPartida) {
        this.dirPartida = dirPartida;
    }

    @XmlElement(required=true)
    public String getDirPartida() {
        return dirPartida;
    }

    public void setRazonSocialTransportista(String razonSocialTransportista) {
        this.razonSocialTransportista = razonSocialTransportista;
    }

    @XmlElement(required=true)
    public String getRazonSocialTransportista() {
        return razonSocialTransportista;
    }

    public void setTipoIdentificacionTransportista(String tipoIdentificacionTransportista) {
        this.tipoIdentificacionTransportista = tipoIdentificacionTransportista;
    }

    @XmlElement(required=true)
    public String getTipoIdentificacionTransportista() {
        return tipoIdentificacionTransportista;
    }

    public void setRucTransportista(String rucTransportista) {
        this.rucTransportista = rucTransportista;
    }

    @XmlElement(required=true)
    public String getRucTransportista() {
        return rucTransportista;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getRise() {
        return rise;
    }

    public void setFechaIniTransporte(String fechaIniTransporte) {
        this.fechaIniTransporte = fechaIniTransporte;
    }

    @XmlElement(required=true)
    public String getFechaIniTransporte() {
        return fechaIniTransporte;
    }

    public void setFechaFinTransporte(String fechaFinTransporte) {
        this.fechaFinTransporte = fechaFinTransporte;
    }

    @XmlElement(required=true)
    public String getFechaFinTransporte() {
        return fechaFinTransporte;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @XmlElement(required=true)
    public String getPlaca() {
        return placa;
    }
    
//    public void addDestinatario(DestinatarioRemision destinatario)
//    {
//        destinatarios.add(destinatario);                    
//    }
//    
//    public int getDestinatariosSize()
//    {
//        return destinatarios.size();
//    }
//    
//    public DestinatarioRemision getDestinatario(int index)
//    {
//        return (DestinatarioRemision)destinatarios.get(index);
//    }
    
    public void setDestinatarios(DestinatariosRemision destinatarios) {
        this.destinatarios = destinatarios;
    }

    @XmlElement(required=true)
    public DestinatariosRemision getDestinatarios() 
    {
        if (destinatarios == null) 
        {
            destinatarios = new DestinatariosRemision();
        }
        return destinatarios;
    }
}

