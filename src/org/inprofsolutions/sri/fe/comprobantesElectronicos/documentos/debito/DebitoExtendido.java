/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.motivos.MotivosDebito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;

/**
 *
 * @author caf
 */
public class DebitoExtendido extends Debito
{
    
    @Override
    public void setMotivosDebito(MotivosDebito motivosDebito)
    {
        super.setMotivosDebito(motivosDebito);
        
        if(getDebitoImpuestos().getDebitoImpuesto().size() == 0)
        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"getTotalConImpuestos().getImpuesto().size() == 0");
            if(motivosDebito.getMotivoDebito().get(motivosDebito.getMotivoDebito().size()-1).getMotivosImpuestos().getMotivoImpuesto().size() == 0 )
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"Error: (last).getMotivosImpuestos().getMotivoImpuesto().size() = " + motivosDebito.getMotivoDebito().get(motivosDebito.getMotivoDebito().size()-1).getMotivosImpuestos().getMotivoImpuesto().size() + "; Deberia ser mayor a cero");
            }            
            
            for(int i = 0 ; i < motivosDebito.getMotivoDebito().get(motivosDebito.getMotivoDebito().size()-1).getMotivosImpuestos().getMotivoImpuesto().size() ; i++)
            {
                getDebitoImpuestos().getDebitoImpuesto().add(motivosDebito.getMotivoDebito().get(motivosDebito.getMotivoDebito().size()-1).getMotivosImpuestos().getMotivoImpuesto().get(i));
            }
        }
        else
        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"getTotalConImpuestos().getImpuesto().size() != 0");            
            for(int i = 0; i < motivosDebito.getMotivoDebito().get(motivosDebito.getMotivoDebito().size()-1).getMotivosImpuestos().getMotivoImpuesto().size();i++)
            {
//                LoggerContainer.getListLogger().get(0).log(Level.INFO,"i = " + i);
                Impuesto impuestoDetalleTmp = motivosDebito.getMotivoDebito().get(motivosDebito.getMotivoDebito().size()-1).getMotivosImpuestos().getMotivoImpuesto().get(i);
                for(int j = 0; j < getDebitoImpuestos().getDebitoImpuesto().size();j++)
                {
//                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"i = " + i + ",j = " + j);
                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                    simbolos.setDecimalSeparator('.');
                    DecimalFormat formatoDecimal = new DecimalFormat("0.00",simbolos);
                    
                    Impuesto impuestoTotalTmp = getDebitoImpuestos().getDebitoImpuesto().get(j);
                    
                    //Container.getListLogger().get(0).log(Level.INFO,impuestoTotalTmp.getCodigo()+ " "  + impuestoDetalleTmp.getCodigo()+ " "  + impuestoTotalTmp.getCodigoPorcentaje() + " " + impuestoDetalleTmp.getCodigoPorcentaje());
                    if((impuestoTotalTmp.getCodigo().equals(impuestoDetalleTmp.getCodigo()) & (impuestoTotalTmp.getCodigoPorcentaje().equals(impuestoDetalleTmp.getCodigoPorcentaje()))))
                    {
                        getDebitoImpuestos().getDebitoImpuesto().remove(impuestoTotalTmp);
                        
                        String baseImponible = formatoDecimal.format(new Double(impuestoTotalTmp.getBaseImponible()).doubleValue() + new Double(impuestoDetalleTmp.getBaseImponible()).doubleValue()); 
                        String valor = formatoDecimal.format(new Double(impuestoTotalTmp.getValor()).doubleValue() + new Double(impuestoDetalleTmp.getValor()).doubleValue()); 

//                        LoggerContainer.getListLogger().get(0).log(Level.INFO,"baseImponible despues= " + baseImponible);
//                        LoggerContainer.getListLogger().get(0).log(Level.INFO,"valor despues= " + valor);
                        
                        Impuesto impuestoTotalTmp1 = new Impuesto(impuestoTotalTmp.getCodigo(), impuestoTotalTmp.getCodigoPorcentaje(), impuestoTotalTmp.getTarifa(), baseImponible, valor);

                        getDebitoImpuestos().getDebitoImpuesto().add(impuestoTotalTmp1);
                        
                        j = getDebitoImpuestos().getDebitoImpuesto().size();
                    }
                    else
                    {
                        if(j == getDebitoImpuestos().getDebitoImpuesto().size()-1)
                        {
                            getDebitoImpuestos().getDebitoImpuesto().add(impuestoDetalleTmp);
                            
                            j = getDebitoImpuestos().getDebitoImpuesto().size();
                        }
                    }
                }
            }
        }            
    }
}
