/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class DetallesFactura 
{
    private List<DetalleFactura> detalleFactura;

    public void setDetalleFactura(List<DetalleFactura> detalleFactura) {
        this.detalleFactura = detalleFactura;
    }

    @XmlElement(required=true)
    public List<DetalleFactura> getDetalleFactura() 
    {
        if (detalleFactura == null) 
        {
            detalleFactura = new ArrayList<DetalleFactura>();
        }
        return detalleFactura;
    }
}
