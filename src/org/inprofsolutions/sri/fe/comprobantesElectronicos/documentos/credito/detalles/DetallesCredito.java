/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class DetallesCredito 
{
    private List<DetalleCredito> detalleCredito;

    public void setDetalleCredito(List<DetalleCredito> detalleCredito) {
        this.detalleCredito = detalleCredito;
    }

    @XmlElement(required=true)
    public List<DetalleCredito> getDetalleCredito() {
        if (detalleCredito == null) 
        {
            detalleCredito = new ArrayList<DetalleCredito>();
        }
        return detalleCredito;
    }
}
