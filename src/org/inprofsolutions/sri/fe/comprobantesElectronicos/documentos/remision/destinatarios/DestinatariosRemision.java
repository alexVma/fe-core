/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.destinatarios;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class DestinatariosRemision 
{
    private List<DestinatarioRemision> destinatarioRemision;

    public void setDestinatarioRemision(List<DestinatarioRemision> destinatarioRemision) {
        this.destinatarioRemision = destinatarioRemision;
    }

    @XmlElement(required=true)
    public List<DestinatarioRemision> getDestinatarioRemision() {
        if (destinatarioRemision == null) 
        {
            destinatarioRemision = new ArrayList<DestinatarioRemision>();
        }
        return destinatarioRemision;
    }
}
