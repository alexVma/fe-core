/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.impuestos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;

/**
 *
 * @author caf
 */
public class ImpuestosDetalle 
{
    private List<Impuesto> impuestoDetalle;

    public void setImpuestoDetalle(List<Impuesto> impuestoDetalle) {
        this.impuestoDetalle = impuestoDetalle;
    }

    @XmlElement(required=true)
    public List<Impuesto> getImpuestoDetalle() {
        if (impuestoDetalle == null) 
        {
            impuestoDetalle = new ArrayList<Impuesto>();
        }
        return impuestoDetalle;
    }
}
