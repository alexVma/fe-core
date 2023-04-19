/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.integrator.baseCSV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author caf
 */
public class GeneradorCSV 
{
    public static final String cabeceraFactura = "ambiente;tipoEmision;razonSocial;nombreComercial;RUC;claveAcceso;codDoc;estab;ptoEmi;secuencial;direccionMatriz;fechaDeEmision;dirEstablecimiento;contribuyenteEspecial;obligadoContabilidad;tipoIdentficaconComprador;guiaRemision;razonSocialComprador;identificacionComprador;valorTotalSinImpuestos;valorTotalDescuentos;propina;importeTotal;moneda;campoAdicional:email";
    public static final String detalleFactura = "codigo;descripcion;cantidad;precio;descuento;precioSinImpuestos;codigoImpuestoIVA;codigoPorcentajeIVA;tarifaIVA;baseImponibleIVA;valorIVA;codigoImpuestoICE;codigoPorcentajeICE;tarifaICE;baseImponibleICE;valorICE";
    
    public static final String cabeceraRetencion = "ambiente;tipoEmision;razonSocial;nombreComercial;RUC;claveAcceso;codDoc;estab;ptoEmi;secuencial;direccionMatriz;fechaDeEmision;dirEstablecimiento;contribuyenteEspecial;obligadoContabilidad;tipoIdentficaconSujetoRetenido;razonSocialSujetoRetenido;identificacionSujetoRetenido;periodoFiscal;campoAdicional:email";
    public static final String detalleRetencion = "codigo;codigoRetencion;baseImponible;porcentajeRetener;valorRetenido;codDocSustento;numDocSustento;fechaEmisionDocSustento";
    
    public static final String cabeceraRemision = "ambiente;tipoEmision;razonSocial;nombreComercial;RUC;claveAcceso;codDoc;estab;ptoEmi;secuencial;direccionMatriz;fechaDeEmision;dirEstablecimiento;dirPartida;razonSocialTransportista;tipoIdentificacionTransportista;rucTransportista;rise;obligadoContabilidad;contribuyenteEspecial;fechaIniTransporte;fechaFinTransporte;placa;campoAdicional:email";
    public static final String detalleRemision = "identificacionDestinatario;razonSocialDestinatario;dirDestinatario;motivoTraslado;docAduaneroUnico;codEstabDestino;ruta;codDocSustento;numDocSustento;numAutDocSustento;fechaEmisionDocSustento;codigoInterno;codigoAdicional;descripcion;cantidad;detAdicional:nombre;detAdicional:valor;detAdicional:nombre;detAdicional:valor;detAdicional:nombre;detAdicional:valor";
    
    public static final String cabeceraCredito = "ambiente;tipoEmision;razonSocial;nombreComercial;RUC;claveAcceso;codDoc;estab;ptoEmi;secuencial;direccionMatriz;fechaDeEmision;dirEstablecimiento;tipoIdentficaconComprador;razonSocialComprador;identificacionComprador;contribuyenteEspecial;obligadoContabilidad;rise;codDocModificado;numDocModificado;fechaEmisionDocSustento;totalSinImpuestos;valorModificacion;moneda;motivo;campoAdicional:email";
    public static final String detalleCredito = "codigoInterno;descripcion;cantidad;precioUnitario;descuento;precioTotalSinImpuestos;codigoImpuestoIVA;codigoPorcentajeIVA;tarifaIVA;baseImponibleIVA;valorIVA;codigoImpuestoICE;codigoPorcentajeICE;tarifaICE;baseImponibleICE;valorICE";
    
    public static final String cabeceraDebito = "ambiente;tipoEmision;razonSocial;nombreComercial;RUC;claveAcceso;codDoc;estab;ptoEmi;secuencial;direccionMatriz;fechaDeEmision;dirEstablecimiento;tipoIdentficaconComprador;razonSocialComprador;identificacionComprador;contribuyenteEspecial;obligadoContabilidad;rise;codDocModificado;numDocModificado;fechaEmisionDocSustento;totalSinImpuestos;valorTotal;campoAdicional:email";
    public static final String detalleDebito = "razon;valor;codigoImpuestoIVA;codigoPorcentajeIVA;tarifaIVA;baseImponibleIVA;valorIVA";
    
    public static void GenerarFactura(String path,String [] cabecera, String [][] detalle) throws FileNotFoundException, IOException
    {
        String facturaNumero = cabecera[7]+"-"+cabecera[8]+"-"+cabecera[9];
        File fileCabecera = new File(path,"cabecera-codDoc"+cabecera[6]+"-docNo"+facturaNumero+".csv");
        FileOutputStream fos = new FileOutputStream(fileCabecera);
        String stringCabecera = "";
        for(int i = 0; i < cabecera.length;i++)
        {
            stringCabecera = stringCabecera + cabecera[i];
            if(i < cabecera.length-1 )
            {
                stringCabecera = stringCabecera + ";";
            }
        }
        fos.write((cabeceraFactura+"\r\n"+stringCabecera).getBytes());
        fos.close();
        
        File fileDetalle = new File(path,"detalle-codDoc"+cabecera[6]+"-docNo"+facturaNumero+".csv");
        fos = new FileOutputStream(fileDetalle);
        String stringDetalle = "";
        
        for(int i = 0 ; i < detalle.length; i++)
        {
            for(int j = 0 ; j < detalle[i].length; j++)
            {
                stringDetalle = stringDetalle + detalle[i][j];
                if(j < detalle[i].length-1 )
                {
                    stringDetalle = stringDetalle + ";";
                }
            }
            if(i < detalle[i].length-1 )
            {

                stringDetalle = stringDetalle + "\r\n";
            }
        }
        
        fos.write((detalleFactura+"\r\n"+stringDetalle).getBytes());
        fos.close();
    }
    
    public static void GenerarRetencion(String path,String [] cabecera, String [][] detalle) throws FileNotFoundException, IOException
    {
        String facturaNumero = cabecera[7]+"-"+cabecera[8]+"-"+cabecera[9];
        File fileCabecera = new File(path,"cabecera-codDoc"+cabecera[6]+"-docNo"+facturaNumero+".csv");
        FileOutputStream fos = new FileOutputStream(fileCabecera);
        String stringCabecera = "";
        for(int i = 0; i < cabecera.length;i++)
        {
            stringCabecera = stringCabecera + cabecera[i];
            if(i < cabecera.length-1 )
            {
                stringCabecera = stringCabecera + ";";
            }
        }
        fos.write((cabeceraRetencion+"\r\n"+stringCabecera).getBytes());
        fos.close();
        
        File fileDetalle = new File(path,"detalle-codDoc"+cabecera[6]+"-docNo"+facturaNumero+".csv");
        fos = new FileOutputStream(fileDetalle);
        String stringDetalle = "";
        
        for(int i = 0 ; i < detalle.length; i++)
        {
            for(int j = 0 ; j < detalle[i].length; j++)
            {
                stringDetalle = stringDetalle + detalle[i][j];
                if(j < detalle[i].length-1 )
                {
                    stringDetalle = stringDetalle + ";";
                }
            }
            if(i < detalle[i].length-1 )
            {

                stringDetalle = stringDetalle + "\r\n";
            }
        }
        
        fos.write((detalleRetencion+"\r\n"+stringDetalle).getBytes());
        fos.close();
    }
    
    public static void GenerarRemision(String path,String [] cabecera, String [][] detalle) throws FileNotFoundException, IOException
    {
        String remisionNumero = cabecera[7]+"-"+cabecera[8]+"-"+cabecera[9];
        File fileCabecera = new File(path,"cabecera-codDoc"+cabecera[6]+"-docNo"+remisionNumero+".csv");
        FileOutputStream fos = new FileOutputStream(fileCabecera);
        String stringCabecera = "";
        for(int i = 0; i < cabecera.length;i++)
        {
            stringCabecera = stringCabecera + cabecera[i];
            if(i < cabecera.length-1 )
            {
                stringCabecera = stringCabecera + ";";
            }
        }
        fos.write((cabeceraRemision+"\r\n"+stringCabecera).getBytes());
        fos.close();
        
        File fileDetalle = new File(path,"detalle-codDoc"+cabecera[6]+"-docNo"+remisionNumero+".csv");
        fos = new FileOutputStream(fileDetalle);
        String stringDetalle = "";
        
        for(int i = 0 ; i < detalle.length; i++)
        {
            for(int j = 0 ; j < detalle[i].length; j++)
            {
                stringDetalle = stringDetalle + detalle[i][j];
                if(j < detalle[i].length-1 )
                {
                    stringDetalle = stringDetalle + ";";
                }
            }
            if(i < detalle[i].length-1 )
            {

                stringDetalle = stringDetalle + "\r\n";
            }
        }
        
        fos.write((detalleRemision+"\r\n"+stringDetalle).getBytes());
        fos.close();
    }
    
    public static void GenerarCredito(String path,String [] cabecera, String [][] detalle) throws FileNotFoundException, IOException
    {
        String creditoNumero = cabecera[7]+"-"+cabecera[8]+"-"+cabecera[9];
        File fileCabecera = new File(path,"cabecera-codDoc"+cabecera[6]+"-docNo"+creditoNumero+".csv");
        FileOutputStream fos = new FileOutputStream(fileCabecera);
        String stringCabecera = "";
        for(int i = 0; i < cabecera.length;i++)
        {
            stringCabecera = stringCabecera + cabecera[i];
            if(i < cabecera.length-1 )
            {
                stringCabecera = stringCabecera + ";";
            }
        }
        fos.write((cabeceraCredito+"\r\n"+stringCabecera).getBytes());
        fos.close();
        
        File fileDetalle = new File(path,"detalle-codDoc"+cabecera[6]+"-docNo"+creditoNumero+".csv");
        fos = new FileOutputStream(fileDetalle);
        String stringDetalle = "";
        
        for(int i = 0 ; i < detalle.length; i++)
        {
            for(int j = 0 ; j < detalle[i].length; j++)
            {
                stringDetalle = stringDetalle + detalle[i][j];
                if(j < detalle[i].length-1 )
                {
                    stringDetalle = stringDetalle + ";";
                }
            }
            if(i < detalle[i].length-1 )
            {

                stringDetalle = stringDetalle + "\r\n";
            }
        }
        
        fos.write((detalleCredito+"\r\n"+stringDetalle).getBytes());
        fos.close();
    }
    
    public static void GenerarDebito(String path,String [] cabecera, String [][] detalle) throws FileNotFoundException, IOException
    {
        String debitoNumero = cabecera[7]+"-"+cabecera[8]+"-"+cabecera[9];
        File fileCabecera = new File(path,"cabecera-codDoc"+cabecera[6]+"-docNo"+debitoNumero+".csv");
        FileOutputStream fos = new FileOutputStream(fileCabecera);
        String stringCabecera = "";
        for(int i = 0; i < cabecera.length;i++)
        {
            stringCabecera = stringCabecera + cabecera[i];
            if(i < cabecera.length-1 )
            {
                stringCabecera = stringCabecera + ";";
            }
        }
        fos.write((cabeceraDebito+"\r\n"+stringCabecera).getBytes());
        fos.close();
        
        File fileDetalle = new File(path,"detalle-codDoc"+cabecera[6]+"-docNo"+debitoNumero+".csv");
        fos = new FileOutputStream(fileDetalle);
        String stringDetalle = "";
        
        for(int i = 0 ; i < detalle.length; i++)
        {
            for(int j = 0 ; j < detalle[i].length; j++)
            {
                stringDetalle = stringDetalle + detalle[i][j];
                if(j < detalle[i].length-1 )
                {
                    stringDetalle = stringDetalle + ";";
                }
            }
            if(i < detalle[i].length-1 )
            {

                stringDetalle = stringDetalle + "\r\n";
            }
        }
        
        fos.write((detalleDebito+"\r\n"+stringDetalle).getBytes());
        fos.close();
    }
}
