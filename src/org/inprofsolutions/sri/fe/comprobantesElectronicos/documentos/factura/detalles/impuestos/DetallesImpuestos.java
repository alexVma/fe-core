/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.impuestos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;

/**
 *
 * @author caf
 */
public class DetallesImpuestos 
{
    private List<Impuesto> detalleImpuesto;

    public void setDetalleImpuesto(List<Impuesto> detalleImpuesto) {
        this.detalleImpuesto = detalleImpuesto;
    }

    @XmlElement(required=true)
    public List<Impuesto> getDetalleImpuesto() 
    {
        if (detalleImpuesto == null) 
        {
            detalleImpuesto = new ArrayList<Impuesto>();
        }
        return detalleImpuesto;
    }
}
