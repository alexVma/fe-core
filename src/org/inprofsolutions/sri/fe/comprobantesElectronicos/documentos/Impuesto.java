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
public class Impuesto 
{
    private String codigo = null;
    private String codigoPorcentaje = null;
    private String baseImponible = null;
    private String valor = null;
    private String tarifa = null;

    public Impuesto() {
    }
    
    public Impuesto(String codigo, String codigoPorcentaje, String tarifa, String baseImponible, String valor) {
        setCodigo(codigo);
        setCodigoPorcentaje(codigoPorcentaje);
        setBaseImponible(baseImponible);
        setValor(valor);
        setTarifa(tarifa);
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @XmlElement(required=true)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigoPorcentaje(String codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }

    @XmlElement(required=true)
    public String getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setBaseImponible(String baseImponible) {
        this.baseImponible = baseImponible;
    }

    @XmlElement(required=true)
    public String getBaseImponible() {
        return baseImponible;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @XmlElement(required=true)
    public String getValor() {
        return valor;
    }
    
    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    @XmlElement(required=true)
    public String getTarifa() {
        return tarifa;
    }
}
