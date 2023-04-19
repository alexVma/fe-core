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
public abstract class Tabla2 
{
    static String EmisionNormal = "1";
    static String EmisionPorIndisponibilidaddelSistema = "2";
    
    public static String getLabel(String parameter)
    {
        if(parameter != null)
        {
            if(parameter.equals("1"))
            {
                return "Normal";
            }
            else if(parameter.equals("2"))
            {
                return "Por indisponibilidad del sistema";
            }
        }
        return null;
    }
}
