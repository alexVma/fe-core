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
public abstract class Tabla24 
{
    static final String SIN_UTILIZACION_DEL_SISTEMA_FINANCIERO = "01";
    static final String CHEQUE_PROPIO = "02";
    static final String CHEQUE_CERTIFICADO = "03";
    static final String CHEQUE_DE_GERENCIA = "04";
    static final String CHEQUE_DEL_EXTERIOR = "05";
    
    static final String DEBITO_DE_CUENTA = "06";
    static final String TRANSFERENCIA_PROPIO_BANCO = "07";
    static final String TRANSFERENCIA_OTRO_BANCO_NACIONAL = "08";
    static final String TRANSFERENCIA_BANCO_EXTERIOR = "09";
    static final String TARJETA_DE_CREDITO_NACIONAL = "10";
    static final String TARJETA_DE_CREDITO_INTERNACIONAL = "11";
    static final String GIRO = "12";
    static final String DEPOSITO_EN_CUENTA_CORRIENTE_AHORROS = "13";
    static final String ENDOSO_DE_INVERSION = "14";
    static final String COMPENSACION_DE_DEUDAS = "15";
    
    
    public static String getLabel(String parameter)
    {
        if(parameter != null)
        {
            if(parameter.equals("01"))
            {
                return "SIN UTILIZACION DEL SISTEMA FINANCIERO";
            }
            else if(parameter.equals("02"))
            {
                return "CHEQUE PROPIO";
            }
            else if(parameter.equals("03"))
            {
                return "CHEQUE CERTIFICADO";
            }
            else if(parameter.equals("04"))
            {
                return "CHEQUE DE GERENCIA";
            }
            else if(parameter.equals("05"))
            {
                return "CHEQUE DEL EXTERIOR";
            }
            else if(parameter.equals("06"))
            {
                return "DÉBITO DE CUENTA";
            }
            else if(parameter.equals("07"))
            {
                return "TRANSFERENCIA PROPIO BANCO";
            }
            else if(parameter.equals("08"))
            {
                return "TRANSFERENCIA OTRO BANCO NACIONAL";
            }
            else if(parameter.equals("09"))
            {
                return "TRANSFERENCIA BANCO EXTERIOR";
            }
            else if(parameter.equals("10"))
            {
                return "TARJETA DE CRÉDITO NACIONAL";
            }
            else if(parameter.equals("11"))
            {
                return "TARJETA DE CRÉDITO INTERNACIONAL";
            }
            else if(parameter.equals("12"))
            {
                return "GIRO";
            }
            else if(parameter.equals("13"))
            {
                return "DEPOSITO EN CUENTA (CORRIENTE/AHORROS)";
            }
            else if(parameter.equals("14"))
            {
                return "ENDOSO DE INVERSIÓN";
            }
            else if(parameter.equals("15"))
            {
                return "COMPENSACIÓN DE DEUDAS";
            }
        }
        return "";
    }
    
}
