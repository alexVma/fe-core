/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.detalles.DetallesCredito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;

/**
 *
 * @author caf
 */
public class CreditoExtendido extends Credito
{
    
    @Override
    public void setDetallesCredito(DetallesCredito detallesCredito)
    {
        super.setDetallesCredito(detallesCredito);
        
        if(getTotalConImpuestos().getImpuesto().size() == 0)
        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"getTotalConImpuestos().getImpuesto().size() == 0");
            if(detallesCredito.getDetalleCredito().get(detallesCredito.getDetalleCredito().size()-1).getImpuestosDetalle().getImpuestoDetalle().size() == 0 )
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"Error: (last)getImpuestosDetalle().getImpuestoDetalle().size() = " + detallesCredito.getDetalleCredito().get(detallesCredito.getDetalleCredito().size()-1).getImpuestosDetalle().getImpuestoDetalle().size()+ "; Deberia ser mayor a cero");
            }
            
            for(int i = 0; i < detallesCredito.getDetalleCredito().get(detallesCredito.getDetalleCredito().size()-1).getImpuestosDetalle().getImpuestoDetalle().size();i++)
            {
                getTotalConImpuestos().getImpuesto().add(detallesCredito.getDetalleCredito().get(detallesCredito.getDetalleCredito().size()-1).getImpuestosDetalle().getImpuestoDetalle().get(i));
            }
        }
        else
        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"getTotalConImpuestos().getImpuesto().size() != 0");
            for(int i = 0; i < detallesCredito.getDetalleCredito().get(detallesCredito.getDetalleCredito().size()-1).getImpuestosDetalle().getImpuestoDetalle().size();i++)
            {
//                LoggerContainer.getListLogger().get(0).log(Level.INFO,"i = " + i);
                Impuesto impuestoDetalleTmp = detallesCredito.getDetalleCredito().get(detallesCredito.getDetalleCredito().size()-1).getImpuestosDetalle().getImpuestoDetalle().get(i);
                for(int j = 0; j < getTotalConImpuestos().getImpuesto().size();j++)
                {
//                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"i = " + i + ",j = " + j);
                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                    simbolos.setDecimalSeparator('.');
                    DecimalFormat formatoDecimal = new DecimalFormat("0.00",simbolos);                    
            
                    Impuesto impuestoTotalTmp = getTotalConImpuestos().getImpuesto().get(j);

//                    LoggerContainer.getListLogger().get(0).log(Level.INFO,impuestoTotalTmp.getCodigo()+ " "  + impuestoDetalleTmp.getCodigo()+ " "  + impuestoTotalTmp.getCodigoPorcentaje() + " " + impuestoDetalleTmp.getCodigoPorcentaje());
                    if((impuestoTotalTmp.getCodigo().equals(impuestoDetalleTmp.getCodigo()) & (impuestoTotalTmp.getCodigoPorcentaje().equals(impuestoDetalleTmp.getCodigoPorcentaje()))))
                    {
                        getTotalConImpuestos().getImpuesto().remove(impuestoTotalTmp);
                        
                        String baseImponible = formatoDecimal.format(new Double(impuestoTotalTmp.getBaseImponible()).doubleValue() + new Double(impuestoDetalleTmp.getBaseImponible()).doubleValue()); 
                        String valor = formatoDecimal.format(new Double(impuestoTotalTmp.getValor()).doubleValue() + new Double(impuestoDetalleTmp.getValor()).doubleValue()); 

//                        LoggerContainer.getListLogger().get(0).log(Level.INFO,"baseImponible despues= " + baseImponible);
//                        LoggerContainer.getListLogger().get(0).log(Level.INFO,"valor despues= " + valor);
                        
                        Impuesto impuestoTotalTmp1 = new Impuesto(impuestoTotalTmp.getCodigo(), impuestoTotalTmp.getCodigoPorcentaje(), impuestoTotalTmp.getTarifa(), baseImponible, valor);
                        
                        getTotalConImpuestos().getImpuesto().add(impuestoTotalTmp1);
                        
                        j = getTotalConImpuestos().getImpuesto().size();
                    }
                    else
                    {
                        if(j == getTotalConImpuestos().getImpuesto().size()-1)
                        {
                            getTotalConImpuestos().getImpuesto().add(impuestoDetalleTmp);
                            
                            j = getTotalConImpuestos().getImpuesto().size();
                        }
                    }
                }
            }
        }            
    }
    
}
