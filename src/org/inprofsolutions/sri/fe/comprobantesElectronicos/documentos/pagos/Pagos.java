/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.pagos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author alexv
 */
public class Pagos {
    private List<Pago> Pago;

    public List<Pago> getPago() {
    if (Pago == null) 
        {
            Pago = new ArrayList<Pago>();
        }
        return Pago;
    }
    
    @XmlElement(required=true)
    public void setPago(List<Pago> Pago) {
        this.Pago = Pago;
    }
    
}
