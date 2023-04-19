/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class MotivosDebito 
{
    private List<MotivoDebito> motivoDebito;

    public void setMotivoDebito(List<MotivoDebito> motivoDebito) {
        this.motivoDebito = motivoDebito;
    }

    @XmlElement(required=true)
    public List<MotivoDebito> getMotivoDebito() {
        if (motivoDebito == null) 
        {
            motivoDebito = new ArrayList<MotivoDebito>();
        }
        return motivoDebito;
    }

    
    
}
