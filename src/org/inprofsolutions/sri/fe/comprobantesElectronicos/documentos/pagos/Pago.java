/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.pagos;

/**
 *
 * @author alexv
 */
public class Pago {
    private String formaPago=null;
    private String total=null;
    private String plazo=null;
    private String unidadTiempo=null;

    public Pago() {
    }

    public Pago(String formaPago,String total,String plazo,String unidadTiempo) {
    this.formaPago=formaPago;
    this.total=total;
    this.plazo=plazo;
    this.unidadTiempo=unidadTiempo;
    }
    
    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public String getUnidadTiempo() {
        return unidadTiempo;
    }

    public void setUnidadTiempo(String unidadTiempo) {
        this.unidadTiempo = unidadTiempo;
    }
}
