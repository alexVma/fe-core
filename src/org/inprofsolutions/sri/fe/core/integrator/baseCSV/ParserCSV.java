/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.integrator.baseCSV;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.mozilla.universalchardet.UniversalDetector;

/**
 *
 * @author caf
 */
public class ParserCSV 
{
    public static String getFilesAsString(File archivo, String taskID) throws FileNotFoundException, IOException
    {
        String archivoString = null;

        FileInputStream fis = new FileInputStream(archivo);
        byte [] archivoByte = new byte[fis.available()];
        fis.read(archivoByte);
        fis.close();
        
        //Charsets supported by Java
        //    CHARSET_ISO_2022_JP
        //    CHARSET_ISO_2022_CN
        //    CHARSET_ISO_2022_KR
        //    CHARSET_ISO_8859_5
        //    CHARSET_ISO_8859_7
        //    CHARSET_ISO_8859_8
        //    CHARSET_BIG5
        //    CHARSET_GB18030
        //    CHARSET_EUC_JP
        //    CHARSET_EUC_KR
        //    CHARSET_EUC_TW
        //    CHARSET_SHIFT_JIS
        //    CHARSET_IBM855
        //    CHARSET_IBM866
        //    CHARSET_KOI8_R
        //    CHARSET_MACCYRILLIC
        //    CHARSET_WINDOWS_1251
        //    CHARSET_WINDOWS_1252
        //    CHARSET_WINDOWS_1253
        //    CHARSET_WINDOWS_1255
        //    CHARSET_UTF_8
        //    CHARSET_UTF_16BE
        //    CHARSET_UTF_16LE
        //    CHARSET_UTF_32BE
        //    CHARSET_UTF_32LE 
        //
        //Charsets NOT supported by Java
        //    CHARSET_HZ_GB_2312
        //    CHARSET_X_ISO_10646_UCS_4_3412
        //    CHARSET_X_ISO_10646_UCS_4_2143 

        UniversalDetector detector = new UniversalDetector(null);// (1)
        detector.handleData(archivoByte, 0, archivoByte.length);// (2)
        detector.dataEnd();// (3)
        String encoding = detector.getDetectedCharset();// (4)
        if (encoding != null) 
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " + "El archivo: "+ archivo.getName() + ": Charset detectado = " + encoding);
            return new String(archivoByte,encoding);
        } 
        else 
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,taskID + " - " + "El archivo: "+ archivo.getName() + ": No se ha podido detectar el Charset.");
        }
        detector.reset(); // (5)
        
        return new String(archivoByte);
    }
    
    public static String [][] parserCSV(File archivo, String taskID) throws IOException
    {
        String archivosString  = getFilesAsString(archivo, taskID);
        String stringFilas [] = archivosString.split("\r\n");
        
        String [][] matriz = new String [stringFilas.length][0];
        for(int i = 0; i < stringFilas.length ;i++)
        {
            String columnasFilaActual [] = stringFilas[i].split(";");
            matriz[i] = columnasFilaActual;
            
        }
        
        return matriz;
    }
    
    
}
