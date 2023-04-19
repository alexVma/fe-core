/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class CampoAdicional 
{
    private String nombre = null; 
    private String valor = null;

    public CampoAdicional() {
    }

    public CampoAdicional(String nombre, String valor) 
    {
        setNombre(nombre);
        setValor(valor);
    }
    
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlElement(required=true)
    public String getNombre() {
        return nombre;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @XmlElement(required=true)
    public String getValor() {
        return valor;
    }
}
