/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos;

import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos.impuestos.MotivosImpuestos;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class MotivoDebito 
{
    private String razon = null; 
    private String valor = null;
    
    //private ArrayList impuestos = new ArrayList();
    private MotivosImpuestos motivosImpuestos = null;

    public MotivoDebito() {
    }

    public MotivoDebito(String razon, String valor) 
    {
        setRazon(razon);
        setValor(valor);
    }
    
    public MotivoDebito(String razon, String valor, Impuesto impuesto) 
    {
        setRazon(razon);
        setValor(valor);

        //addMotivoImpuesto(impuesto);
        getMotivosImpuestos().getMotivoImpuesto().add(impuesto);
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    @XmlElement(required=true)
    public String getRazon() {
        return razon;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @XmlElement(required=true)
    public String getValor() {
        return valor;
    }
    
//    public void addMotivoImpuesto(Impuesto detalleImpuesto)
//    {
//        impuestos.add(detalleImpuesto);
//    }
//    
//    public int getMotivoImpuestoSize()
//    {
//        return impuestos.size();
//    }
//    
//    public Impuesto getMotivoImpuesto(int index)
//    {
//        return (Impuesto)impuestos.get(index);
//    }
    
    public void setMotivosImpuestos(MotivosImpuestos motivosImpuestos) {
        this.motivosImpuestos = motivosImpuestos;
    }

    public MotivosImpuestos getMotivosImpuestos() {
        if (motivosImpuestos == null) 
        {
            motivosImpuestos = new MotivosImpuestos();
        }
        return motivosImpuestos;
    }

    
    
    
}
