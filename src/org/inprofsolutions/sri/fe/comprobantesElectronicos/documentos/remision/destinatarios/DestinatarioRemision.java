/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios;

import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.detalles.DetallesRemision;

/**
 *
 * @author caf
 */
public class DestinatarioRemision 
{
    private String identificacionDestinatario = null;
    private String razonSocialDestinatario = null;
    private String dirDestinatario = null;
    private String motivoTraslado = null;
    private String docAduaneroUnico = null;
    private String codEstabDestino = null;
    private String ruta = null;
    private String codDocSustento = null;
    private String numDocSustento = null;
    private String numAutDocSustento = null;
    private String fechaEmisionDocSustento = null;
    
    //private ArrayList detallesRemision = new ArrayList();
    private DetallesRemision detallesRemision = null;
    
    public DestinatarioRemision() {
    }

    public DestinatarioRemision(String identificacionDestinatario, String razonSocialDestinatario, String dirDestinatario, String motivoTraslado, String docAduaneroUnico, String codEstabDestino, String ruta, String codDocSustento, String numDocSustento, String numAutDocSustento, String fechaEmisionDocSustento) 
    {
        setIdentificacionDestinatario(identificacionDestinatario);
        setRazonSocialDestinatario(razonSocialDestinatario);
        setDirDestinatario(dirDestinatario);
        setMotivoTraslado(motivoTraslado);
        setDocAduaneroUnico(docAduaneroUnico);
        setCodEstabDestino(codEstabDestino);
        setRuta(ruta);
        setCodDocSustento(codDocSustento);
        setNumDocSustento(numDocSustento);
        setNumAutDocSustento(numAutDocSustento);
        setFechaEmisionDocSustento(fechaEmisionDocSustento);
    }
    
//    public DestinatarioRemision(String identificacionDestinatario, String razonSocialDestinatario, String dirDestinatario, String motivoTraslado, String docAduaneroUnico, String codEstabDestino, String ruta, String codDocSustento, String numDocSustento, String numAutDocSustento, String fechaEmisionDocSustento, DetallesRemision detalleRemision) 
//    {
//        setIdentificacionDestinatario(identificacionDestinatario);
//        setRazonSocialDestinatario(razonSocialDestinatario);
//        setDirDestinatario(dirDestinatario);
//        setMotivoTraslado(motivoTraslado);
//        setDocAduaneroUnico(docAduaneroUnico);
//        setCodEstabDestino(codEstabDestino);
//        setRuta(ruta);
//        setCodDocSustento(codDocSustento);
//        setNumDocSustento(numDocSustento);
//        setNumAutDocSustento(numAutDocSustento);
//        setFechaEmisionDocSustento(fechaEmisionDocSustento);
//        setDetallesRemision(detallesRemision);
//        //addDetalle(detalleRemision);
//    }
    
    public void setIdentificacionDestinatario(String identificacionDestinatario) {
        this.identificacionDestinatario = identificacionDestinatario;
    }

    @XmlElement(required=true)
    public String getIdentificacionDestinatario() {
        return identificacionDestinatario;
    }

    public void setRazonSocialDestinatario(String razonSocialDestinatario) {
        this.razonSocialDestinatario = razonSocialDestinatario;
    }

    @XmlElement(required=true)
    public String getRazonSocialDestinatario() {
        return razonSocialDestinatario;
    }

    public void setDirDestinatario(String dirDestinatario) {
        this.dirDestinatario = dirDestinatario;
    }

    @XmlElement(required=true)
    public String getDirDestinatario() {
        return dirDestinatario;
    }

    public void setMotivoTraslado(String motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }

    @XmlElement(required=true)
    public String getMotivoTraslado() {
        return motivoTraslado;
    }

    public void setDocAduaneroUnico(String docAduaneroUnico) {
        this.docAduaneroUnico = docAduaneroUnico;
    }

    public String getDocAduaneroUnico() {
        return docAduaneroUnico;
    }

    public void setCodEstabDestino(String codEstabDestino) {
        this.codEstabDestino = codEstabDestino;
    }

    public String getCodEstabDestino() {
        return codEstabDestino;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setCodDocSustento(String codDocSustento) {
        this.codDocSustento = codDocSustento;
    }

    public String getCodDocSustento() {
        return codDocSustento;
    }

    public void setNumDocSustento(String numDocSustento) {
        this.numDocSustento = numDocSustento;
    }

    public String getNumDocSustento() {
        return numDocSustento;
    }

    public void setNumAutDocSustento(String numAutDocSustento) {
        this.numAutDocSustento = numAutDocSustento;
    }

    public String getNumAutDocSustento() {
        return numAutDocSustento;
    }

    public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public String getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }
    
//    public void addDetalle(DetalleRemision detalleRemision)
//    {
//        detallesRemision.add(detalleRemision);
//    }
//    
//    public int getDetallesSize()
//    {
//        return detallesRemision.size();
//    }
//    
//    public DetalleRemision getDetalle(int index)
//    {
//        return (DetalleRemision)detallesRemision.get(index);
//    }
    
    public void setDetallesRemision(DetallesRemision detallesRemision) {
        this.detallesRemision = detallesRemision;
    }

    @XmlElement(required=true)
    public DetallesRemision getDetallesRemision() {
        if (detallesRemision == null) 
        {
            detallesRemision = new DetallesRemision();
        }
        return detallesRemision;
    }
}