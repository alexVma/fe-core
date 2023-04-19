/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.pagos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class PagosFactura 
{
    private List<PagoFactura> pagoFactura;

    public void setPagoFactura(List<PagoFactura> pagoFactura) {
        this.pagoFactura = pagoFactura;
    }

    @XmlElement(required=true)
    public List<PagoFactura> getPagoFactura() 
    {
        if (pagoFactura == null) 
        {
            pagoFactura = new ArrayList<PagoFactura>();
        }
        return pagoFactura;
    }
}
