/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.integrator.baseCSV;

import java.io.File;

/**
 *
 * @author caf
 */
public class Carpetas 
{
//    public static final String  ficheroscsvpendientes = "GENERADOS";
//    public static final String  ficheroscsvprocesando = "tmp";
//    public static final String  ficheroscsvprocesados = "AUTORIZADOS";
//    public static final String  autorizados = "pdfxml";
//    public static final String  respuestas = "respuestas";
////    public static final String  logs = "logs";
    
    public static final String  ficheroscsvpendientes = "ficheroscsvpendientes";
    public static final String  ficheroscsvprocesando = "ficheroscsvprocesando";
    public static final String  ficheroscsvprocesados = "ficheroscsvprocesados";
    public static final String  autorizados = "autorizados";
    public static final String  respuestas = "respuestas";
    public static final String  log = "log";
    
    public static boolean crearCarpetaCSVs(String Path)
    {
        File carpeta = new File(Path,ficheroscsvpendientes);
        
        if(!carpeta.exists())
        {
            boolean res = carpeta.mkdir();
            if(res == false)
            {
                return false;
            }
            
        }
        
        carpeta = new File(Path,ficheroscsvprocesando);
        if(!carpeta.exists())
        {
            boolean res = carpeta.mkdir();
            if(res == false)
            {
                return false;
            }
            
        }
        
        carpeta = new File(Path,ficheroscsvprocesados);
        if(!carpeta.exists())
        {
            boolean res = carpeta.mkdir();
            if(res == false)
            {
                return false;
            }
            
        }
        
        carpeta = new File(Path,autorizados);
        if(!carpeta.exists())
        {
            boolean res = carpeta.mkdir();
            if(res == false)
            {
                return false;
            }
        }
        
        carpeta = new File(Path,respuestas);
        if(!carpeta.exists())
        {
            boolean res = carpeta.mkdir();
            if(res == false)
            {
                return false;
            }
        }
        
        carpeta = new File(Path,log);
        if(!carpeta.exists())
        {
            boolean res = carpeta.mkdir();
            if(res == false)
            {
                return false;
            }
        }
        
        return true;
    }
    

    
    
}
