/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos.impuestos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;

/**
 *
 * @author caf
 */
public class DebitoImpuestos {
    private List<Impuesto> debitoImpuesto;

    public void setDebitoImpuesto(List<Impuesto> debitoImpuesto) {
        this.debitoImpuesto = debitoImpuesto;
    }

    @XmlElement(required=true)
    public List<Impuesto> getDebitoImpuesto() {
        if (debitoImpuesto == null) 
        {
            debitoImpuesto = new ArrayList<Impuesto>();
        }
        return debitoImpuesto;
    }
}
