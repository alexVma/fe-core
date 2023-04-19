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
public class InfoAdicional 
{
    private List<CampoAdicional> camposAdicionales;

    public void setCamposAdicionales(List<CampoAdicional> camposAdicionales) 
    {
        this.camposAdicionales = camposAdicionales;
    }

    @XmlElement(required=true)
    public List<CampoAdicional> getCamposAdicionales() 
    {
        if (camposAdicionales == null) 
        {
            camposAdicionales = new ArrayList<CampoAdicional>();
        }
        return camposAdicionales;
    }
}
