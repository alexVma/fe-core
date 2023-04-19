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
public abstract class Tabla17 
{
    static String RENTA = "1";
    static String IVA = "2";
    static String ISD = "6";
    
    public static String getLabel(String parameter)
    {
        if(parameter != null)
        {
            if(parameter.equals("1"))
            {
                return "RENTA";
            }
            else if(parameter.equals("2"))
            {
                return "IVA";
            }
            else if(parameter.equals("6"))
            {
                return "ISD";
            }
        }
        return null;
    }
    
}
