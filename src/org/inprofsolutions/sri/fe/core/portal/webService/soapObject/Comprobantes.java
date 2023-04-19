/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.portal.webService.soapObject;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class Comprobantes 
{
    private List<DefaultBaseComprobante> comprobantes;

    public void setComprobantes(List<DefaultBaseComprobante> comprobantes) {
        this.comprobantes = comprobantes;
    }

    @XmlElement(required=true)
    public List<DefaultBaseComprobante> getComprobantes() 
    {
        if (comprobantes == null) 
        {
            comprobantes = new ArrayList<DefaultBaseComprobante>();
        }
        return comprobantes;
    }
}
