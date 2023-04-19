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
public class MotivosImpuestos 
{
    private List<Impuesto> motivoImpuesto;

    public void setMotivoImpuesto(List<Impuesto> motivoImpuesto) {
        this.motivoImpuesto = motivoImpuesto;
    }

    @XmlElement(required=true)
    public List<Impuesto> getMotivoImpuesto() {
        if (motivoImpuesto == null) 
        {
            motivoImpuesto = new ArrayList<Impuesto>();
        }
        return motivoImpuesto;
    }
}
