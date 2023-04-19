/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.impuestos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class ImpuestosRetencion 
{
    private List<ImpuestoRetencion> impuestoRetencion;

    public void setImpuestoRetencion(List<ImpuestoRetencion> impuestosRetencion) 
    {
        this.impuestoRetencion = impuestosRetencion;
    }

    @XmlElement(required=true)
    public List<ImpuestoRetencion> getImpuestoRetencion() 
    {
        if (impuestoRetencion == null) 
        {
            impuestoRetencion = new ArrayList<ImpuestoRetencion>();
        }
        return impuestoRetencion;
    }
}
