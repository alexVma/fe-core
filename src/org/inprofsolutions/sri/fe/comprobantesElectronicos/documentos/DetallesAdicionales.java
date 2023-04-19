/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class DetallesAdicionales 
{
    private List<DetalleAdicional> detalleAdicional;

    public void setDetalleAdicional(List<DetalleAdicional> detalleAdicional) {
        this.detalleAdicional = detalleAdicional;
    }

    @XmlElement(required=true)
    public List<DetalleAdicional> getDetalleAdicional() 
    {
        if (detalleAdicional == null) 
        {
            detalleAdicional = new ArrayList<DetalleAdicional>();
        }
        return detalleAdicional;
    }
}
