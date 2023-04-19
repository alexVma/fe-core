/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios.detalles;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class DetallesRemision 
{
    private List<DetalleRemision> detalleRemision;

    public void setDetalleRemision(List<DetalleRemision> detalleRemision) {
        this.detalleRemision = detalleRemision;
    }

    @XmlElement(required=true)
    public List<DetalleRemision> getDetalleRemision() 
    {
        if (detalleRemision == null) 
        {
            detalleRemision = new ArrayList<DetalleRemision>();
        }
        return detalleRemision;
    }
    
}
