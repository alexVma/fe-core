/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.core.portal.webService.soapObject;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author caf
 */
public class ListaComprobantes 
{
    boolean login = false;
    Comprobantes comprobantes = null;

    public ListaComprobantes() {
    }

    public ListaComprobantes(boolean login) 
    {
        this.login = login;
    }
    
    public void setComprobantes(Comprobantes comprobantes) {
        this.comprobantes = comprobantes;
    }

    @XmlElement(required=true)
    public Comprobantes getComprobantes() {
        if (comprobantes == null) 
        {
            comprobantes = new Comprobantes();
        }
        return comprobantes;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    @XmlElement(required=true)
    public boolean isLogin() {
        return login;
    }
}
