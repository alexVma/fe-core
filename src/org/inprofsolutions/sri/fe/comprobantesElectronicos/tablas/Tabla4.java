/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas;

/**
 *
 * @author caf
 */
public abstract class Tabla4 
{
    static final String FACTURA = "01";
    static final String NOTADECREDITO = "04";
    static final String NOTADEDEBITO = "05";
    static final String GUIADEREMISION = "06";
    static final String COMPROBANTEDERETENCION = "07";
    
    public static String getLabel(String parameter)
    {
        if(parameter != null)
        {
            if(parameter.equals("01"))
            {
                return "FACTURA";
            }
            else if(parameter.equals("04"))
            {
                return "NOTA DE CREDITO";
            }
            else if(parameter.equals("05"))
            {
                return "NOTA DE DEBITO";
            }
            else if(parameter.equals("06"))
            {
                return "GUIA DE REMISION";
            }
            else if(parameter.equals("07"))
            {
                return "COMPROBANTE DE RETENCION";
            }
        }
        return "";
    }
    
}
