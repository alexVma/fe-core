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
public abstract class Tabla5 
{
    static final String Pruebas = "1";
    static final String Produccion = "2";
    
    public static String getLabel(String parameter)
    {
        if(parameter != null)
        {
            if(parameter.equals("1"))
            {
                return "Pruebas";
            }
            else if(parameter.equals("2"))
            {
                return "Produccion";
            }
        }
        return null;
    }
}
