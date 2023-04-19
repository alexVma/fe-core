/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura;

import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.detalles.DetallesFactura;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.Impuesto;

/**
 *
 * @author caf
 */
public class FacturaExtendida extends Factura
{
    
    @Override
    public void setDetallesFactura(DetallesFactura detallesFactura)
    {
        super.setDetallesFactura(detallesFactura);
        
        if(getTotalConImpuestos().getImpuesto().size() == 0)
        {
            if(detallesFactura.getDetalleFactura().get(detallesFactura.getDetalleFactura().size()-1).getDetallesImpuestos().getDetalleImpuesto().size() == 0 )
            {
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"Error: (last)getDetallesImpuestos().getDetalleImpuesto().size() = " + detallesFactura.getDetalleFactura().get(detallesFactura.getDetalleFactura().size()-1).getDetallesImpuestos().getDetalleImpuesto().size()+ "; Deberia ser mayor a cero");
            }
            
            for(int i = 0; i < detallesFactura.getDetalleFactura().get(detallesFactura.getDetalleFactura().size()-1).getDetallesImpuestos().getDetalleImpuesto().size();i++)
            {
                getTotalConImpuestos().getImpuesto().add(detallesFactura.getDetalleFactura().get(detallesFactura.getDetalleFactura().size()-1).getDetallesImpuestos().getDetalleImpuesto().get(i));   
            }
        }
        else
        {
            for(int i = 0; i < detallesFactura.getDetalleFactura().get(detallesFactura.getDetalleFactura().size()-1).getDetallesImpuestos().getDetalleImpuesto().size();i++)
            {
                Impuesto impuestoDetalleTmp = detallesFactura.getDetalleFactura().get(detallesFactura.getDetalleFactura().size()-1).getDetallesImpuestos().getDetalleImpuesto().get(i);
                for(int j = 0; j < getTotalConImpuestos().getImpuesto().size();j++)
                {
                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                    simbolos.setDecimalSeparator('.');
                    DecimalFormat formatoDecimal = new DecimalFormat("0.00",simbolos);

                    Impuesto impuestoTotalTmp = getTotalConImpuestos().getImpuesto().get(j);

                    if((impuestoTotalTmp.getCodigo().equals(impuestoDetalleTmp.getCodigo()) & (impuestoTotalTmp.getCodigoPorcentaje().equals(impuestoDetalleTmp.getCodigoPorcentaje()))))
                    {
                        getTotalConImpuestos().getImpuesto().remove(impuestoTotalTmp);
                        
                        String baseImponible = formatoDecimal.format(new Double(impuestoTotalTmp.getBaseImponible()).doubleValue() + new Double(impuestoDetalleTmp.getBaseImponible()).doubleValue()); 
                        String valor = formatoDecimal.format(new Double(impuestoTotalTmp.getValor()).doubleValue() + new Double(impuestoDetalleTmp.getValor()).doubleValue()); 
                        
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
