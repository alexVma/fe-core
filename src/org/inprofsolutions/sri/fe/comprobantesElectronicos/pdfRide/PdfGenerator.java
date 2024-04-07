package org.inprofsolutions.sri.fe.comprobantesElectronicos.pdfRide;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.credito.Credito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.debito.Debito;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.Factura;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.datosAdicionales.AguaDatoAdicional;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.exportacion.FacturaExportacion;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.remision.Remision;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.retencion.Retencion;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author caf
 */
public class PdfGenerator 
{   
    String endPage = null;
    
    public PdfGenerator(String endPage) 
    {
        this.endPage = endPage;
    }
    
    class RoundRectangle implements PdfPCellEvent 
    {
        /** the border color described as CMYK values. */
        private int[] color;
        /** Constructs the event using a certain color. */
        private RoundRectangle(int[] color) 
        {
            this.color = color;
        }
 
        @Override
        public void cellLayout(PdfPCell cell, Rectangle rect,
                PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                rect.getHeight() - 3, 4);
            cb.setLineWidth(0.5f);
            cb.setCMYKColorStrokeF(color[0], color[1], color[2], color[3]);
            cb.stroke();
        }
    }
    
    class HeaderFooter extends PdfPageEventHelper 
    {       
        PdfTemplate total;
        
        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            total = writer.getDirectContent().createTemplate(30, 8);
        }
        
        @Override
        public void onEndPage(PdfWriter writer, Document document) 
        {    
            PdfContentByte cb = writer.getDirectContent();
            
            if(endPage.equals("Tipo1"))
                        {
                Phrase line = new Phrase();
                line.add(new LineSeparator(1, 1.2f, null, Element.ALIGN_LEFT, 0));

                ColumnText.showTextAligned(cb,Element.ALIGN_LEFT, line,
                        document.leftMargin(), 
                        document.bottom()-2,0);  

                ColumnText.showTextAligned(cb,Element.ALIGN_LEFT, new Paragraph("Representación Impresa de Documento Electrónico (RIDE) offLine.",FontFactory.getFont("arial", 8)),
                        document.leftMargin(), 
                        document.bottom()-12,0);  

    //            ColumnText.showTextAligned(cb,Element.ALIGN_LEFT, new Paragraph("No posee validez tributaria y no es necesario que la imprima.",FontFactory.getFont("arial", 8)),
    //                    document.leftMargin(), 
    //                    document.bottom()-20,0);  

                PdfPTable paginado = new PdfPTable(2);
                try {
                    paginado.setTotalWidth(new float[]{75,75});
                    paginado.getDefaultCell().setBorder(0);
                    PdfPCell c1 = null;

                    c1 = new PdfPCell(new Phrase(String.format("Página %d de ", writer.getCurrentPageNumber()),FontFactory.getFont("arial", 8)));
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c1.setBorder(0);
                    paginado.addCell(c1);

                    c1 = new PdfPCell(Image.getInstance(total));
                    c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c1.setBorder(0);
                    paginado.addCell(c1);

                    paginado.writeSelectedRows(0, -1,(document.right() - document.left()) / 2 + document.leftMargin()-55, document.bottom()-11, writer.getDirectContent());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                Paragraph propagandaParagraph = new Paragraph();  
                ColumnText.showTextAligned(cb,Element.ALIGN_CENTER, propagandaParagraph,
                        (document.right() - document.left()) / 2 + document.leftMargin(), 
                        document.bottom()-30,0);
                
            }
            else if(endPage.equals("Tipo2"))
                        {
                Phrase line = new Phrase();
                line.add(new LineSeparator(1, 1.2f, null, Element.ALIGN_LEFT, 0));

                ColumnText.showTextAligned(cb,Element.ALIGN_LEFT, line,
                        document.leftMargin(), 
                        document.bottom()-2,0);  

                ColumnText.showTextAligned(cb,Element.ALIGN_LEFT, new Paragraph("Representación Impresa de Documento Electrónico (RIDE) offLine.",FontFactory.getFont("arial", 8)),
                        document.leftMargin(), 
                        document.bottom()-12,0);   

                PdfPTable paginado = new PdfPTable(2);
                try {
                    paginado.setTotalWidth(new float[]{75,75});
                    paginado.getDefaultCell().setBorder(0);
                    PdfPCell c1 = null;

                    c1 = new PdfPCell(new Phrase(String.format("Página %d de ", writer.getCurrentPageNumber()),FontFactory.getFont("arial", 8)));
                    c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c1.setBorder(0);
                    paginado.addCell(c1);

                    c1 = new PdfPCell(Image.getInstance(total));
                    c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c1.setBorder(0);
                    paginado.addCell(c1);

                    paginado.writeSelectedRows(0, -1,(document.right() - document.left()) / 2 + document.leftMargin()-55, document.bottom()-11, writer.getDirectContent());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                Paragraph propagandaParagraph = new Paragraph();  
                ColumnText.showTextAligned(cb,Element.ALIGN_CENTER, propagandaParagraph,
                        (document.right() - document.left()) / 2 + document.leftMargin(), 
                        document.bottom()-30,0);
                
            }
        }
        
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) 
        {   
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,new Phrase(String.valueOf(writer.getPageNumber() - 1),FontFactory.getFont("arial", 8)),1, 0, 0);
        }
    }
    
    private PdfPTable getSeccionCabecera(DefaultComprobanteElectronico comprobanteElectronico, byte [] imagen,XMLGregorianCalendar fechaDeAutorizacion, String NumeroDeAutorizacion, PdfWriter writer) throws BadElementException, IOException 
    {   
        PdfPTable infoEmpresa = new PdfPTable(1);
        infoEmpresa.getDefaultCell().setBorder(0);
        
        Image im = Image.getInstance(imagen);
        
        if(im == null)
        {
            infoEmpresa.addCell(new Phrase(comprobanteElectronico.getNombreComercial().toUpperCase(),FontFactory.getFont("arial", 18)));
        }
        else
        {
            PdfPCell imgCell = new PdfPCell(im);
            imgCell.setFixedHeight(74);
            imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);   
            imgCell.setBorder(0);
            infoEmpresa.addCell(imgCell);        
        }
        
        PdfPCell c1 = new PdfPCell(new Phrase(" "));
        c1.setFixedHeight(10);
        c1.setBorder(0);
        infoEmpresa.addCell(c1);        
        
        PdfPTable infoEmpresaData = new PdfPTable(1);
        infoEmpresaData.getDefaultCell().setBorder(0);
        infoEmpresaData.getDefaultCell().setPaddingTop(5);
        
        infoEmpresaData.addCell(new Phrase(comprobanteElectronico.getRazonSocial(),FontFactory.getFont("arial", 8)));
        infoEmpresaData.addCell(new Phrase("Dir Matriz: " + comprobanteElectronico.getDireccionMatriz(),FontFactory.getFont("arial", 8)));
       // infoEmpresaData.addCell(new Phrase("Dir Sucursal:" + comprobanteElectronico.getDireccionEstablecimiento(),FontFactory.getFont("arial", 8)));
        c1 = new PdfPCell(new Phrase(" "));
        c1.setFixedHeight(10);
        c1.setBorder(0);
        infoEmpresaData.addCell(c1);
        if(comprobanteElectronico.getContribuyenteEspecial() != null)
        {
            if(!comprobanteElectronico.getContribuyenteEspecial().trim().equals("999"))
            {
                infoEmpresaData.addCell(new Phrase("Contribuyente Especial Nro.:" + comprobanteElectronico.getContribuyenteEspecial(),FontFactory.getFont("arial", 8)));
                
            }
        }
        infoEmpresaData.addCell(new Phrase("Obligado a llevar contabilidad: " + comprobanteElectronico.getObligadoALlebarContabilidad(),FontFactory.getFont("arial", 8)));
        
       infoEmpresaData.addCell(new Phrase("AGENTE DE RETENCIÓN:" ,FontFactory.getFont("arial", 8)));
      infoEmpresaData.addCell(new Phrase("RESOLUCIÓN Nro. NAC-DNCRASC20-00000001" ,FontFactory.getFont("arial", 8)));
      
        c1 = new PdfPCell(infoEmpresaData);
        c1.setCellEvent(new RoundRectangle(new int[]{ 0x00, 0x00, 0x00, 0xFF }));
        c1.setPaddingLeft(5);
        c1.setBorder(0);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);        
        infoEmpresa.addCell(c1);
        
        PdfPTable datosDeFactura = new PdfPTable(1);
        datosDeFactura.getDefaultCell().setBorder(0);
        datosDeFactura.getDefaultCell().setPaddingTop(3);
        
        PdfPCell espacio = new PdfPCell(new Phrase(" "));
        espacio.setFixedHeight(5);
        espacio.setBorder(0);        
        datosDeFactura.addCell(new Phrase("R.U.C.: " + comprobanteElectronico.getRuc(),FontFactory.getFont("arial", 10)));
        datosDeFactura.addCell(new Phrase(org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(comprobanteElectronico.getCodigoDocumento()),FontFactory.getFont("arial", 14)));
        datosDeFactura.addCell(new Phrase("No. " + comprobanteElectronico.getEstablecimiento()+ "-" + comprobanteElectronico.getPuntoEmision() + "-" + comprobanteElectronico.getSecuencial(),FontFactory.getFont("arial", 14,BaseColor.RED)));
        datosDeFactura.addCell(espacio);
        datosDeFactura.addCell(new Phrase("NUMERO DE AUTORIZACION:",FontFactory.getFont("arial", 10)));
        c1 = new PdfPCell(new Phrase(NumeroDeAutorizacion,FontFactory.getFont("arial", 10)));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(0);
        c1.setPaddingTop(datosDeFactura.getDefaultCell().getPaddingTop());
        datosDeFactura.addCell(c1);
        datosDeFactura.addCell(espacio);
        if(fechaDeAutorizacion != null)
        {
            datosDeFactura.addCell(new Phrase("FECHA Y HORA DE AUTORIZACIÓN: "+fechaDeAutorizacion.getDay()+"/"+fechaDeAutorizacion.getMonth()+"/"+fechaDeAutorizacion.getYear()+" "+fechaDeAutorizacion.getHour()+":"+fechaDeAutorizacion.getMinute()+":"+fechaDeAutorizacion.getSecond(),FontFactory.getFont("arial", 8))); 
        }
        else
        {
            datosDeFactura.addCell(new Phrase("FECHA Y HORA DE AUTORIZACIÓN: ",FontFactory.getFont("arial", 8))); 
        }
        datosDeFactura.addCell(new Phrase("AMBIENTE: " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla5.getLabel(comprobanteElectronico.getCodigoAmbiente()),FontFactory.getFont("arial", 8)));
        datosDeFactura.addCell(new Phrase("EMISION: " + org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla2.getLabel(comprobanteElectronico.getCodigoTipoEmision()),FontFactory.getFont("arial", 8)));
        datosDeFactura.addCell(espacio);
        datosDeFactura.addCell(new Phrase("CLAVE DE ACCESO:",FontFactory.getFont("arial", 8)));
        Barcode128 code128 = new Barcode128();
        code128.setCode(comprobanteElectronico.getClaveAcceso());
        PdfContentByte cb = writer.getDirectContent();
        c1 = new PdfPCell(code128.createImageWithBarcode(cb, null, null));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(0);
        c1.setPaddingTop(datosDeFactura.getDefaultCell().getPaddingTop());
        datosDeFactura.addCell(c1);
        datosDeFactura.addCell(espacio);
        
        PdfPTable Seccion1 = new PdfPTable(3);  
        Seccion1.getDefaultCell().setBorder(0);
        
        c1 = new PdfPCell(infoEmpresa);
        c1.setBorder(0);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        Seccion1.addCell(c1);
        
        Seccion1.addCell(new Phrase(" "));
        
        c1 = new PdfPCell(datosDeFactura);
        PdfPCellEvent roundRectangle = new RoundRectangle(new int[]{ 0x00, 0x00, 0x00, 0xFF });
        c1.setCellEvent(roundRectangle);
        c1.setPaddingLeft(5);
        c1.setBorder(0);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        Seccion1.addCell(c1);
        
        return Seccion1;
    }
    
    public byte [] getFacturaExportacion(FacturaExportacion factura,XMLGregorianCalendar fechaDeAutorizacion, String NumeroDeAutorizacion, byte [] imagen) throws DocumentException, FileNotFoundException, BadElementException, IOException
    {
        PdfPCell c1 = null;
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A4,36,36,30,40);
        
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        HeaderFooter agregaPieDePagina = new HeaderFooter();
        writer.setPageEvent(agregaPieDePagina);
        
        document.open();
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
                
        PdfPTable Seccion2 = new PdfPTable(1);     
        Seccion2.getDefaultCell().setPadding(3);
        
        PdfPTable DatosCliente = new PdfPTable(4);        
        DatosCliente.getDefaultCell().setBorder(0);
        DatosCliente.addCell(new Phrase("Razón Social / Nombres y Apellidos:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getRazonSocialDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("RUC / CI:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getIdentificacionDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("Fecha Emisión:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getFechaEmision(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("Guía Remisión:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getGuiaRemision(),FontFactory.getFont("arial", 8)));
        
        DatosCliente.setWidths(new float[]{28,43,14,15});
        Seccion2.addCell(DatosCliente);
        
        PdfPTable Seccion3 = new PdfPTable(6);
        Seccion3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
        Seccion3.addCell(new Phrase("Cod. Principal",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Cantidad",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Descripción",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Precio Unitario",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Descuento",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Precio Total",FontFactory.getFont("arial", 8,Font.BOLD)));
        
        for(int i = 0 ; i < factura.getDetallesFactura().getDetalleFactura().size(); i++)
        {
            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getCodigoPrincipal(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getCantidad(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);

            if(!factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().isEmpty())
            {
                PdfPTable detallesAdicionales = new PdfPTable(2);
                detallesAdicionales.getDefaultCell().setBorder(0);
                detallesAdicionales.setWidthPercentage(100);
                detallesAdicionales.setWidths(new float[]{50,50});
                
                PdfPCell cellSpan = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getDescripcion(),FontFactory.getFont("arial", 8)));
                cellSpan.setColspan(2);
                cellSpan.setBorder(0);
                detallesAdicionales.addCell(cellSpan);
                detallesAdicionales.completeRow();
                
                for(int j = 0; j< factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().size();j++)
                {
                    cellSpan = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getNombre(),FontFactory.getFont("arial", 8)));
                    cellSpan.setHorizontalAlignment(Element.ALIGN_CENTER); 
                    cellSpan.setBorder(0);
                    
                    if(factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getValor().indexOf(";") > -1)
                    {
                        cellSpan.setColspan(2);
                        detallesAdicionales.addCell(cellSpan);
                        detallesAdicionales.completeRow();
                    }
                    else
                    {
                        detallesAdicionales.addCell(cellSpan);
                    }
                    
                    String tmp = factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getValor();
                    String detallesAdicionalesArray [] = tmp.split(";");
                    if(detallesAdicionalesArray != null)
                    {
                        for(int k = 0 ; k < detallesAdicionalesArray.length ; k++)
                        {
                            if(detallesAdicionalesArray[k].length() != 0)
                            {
                                String detallesAdicionalesArray1 [] = detallesAdicionalesArray[k].split("=");
                                if(detallesAdicionalesArray1 != null)
                                {
                                    PdfPCell cellPadding = new PdfPCell(new Phrase(detallesAdicionalesArray1[0],FontFactory.getFont("arial", 6)));
                                    cellPadding.setBorder(0);
                                    cellPadding.setPaddingLeft(25);
                                    detallesAdicionales.addCell(cellPadding);
                                    if(detallesAdicionalesArray1.length == 2)
                                    {
                                        detallesAdicionales.addCell(new Phrase(detallesAdicionalesArray1[1],FontFactory.getFont("arial", 6)));
                                    }
                                }
                            }
                        }
                    }
                }
                c1.addElement(detallesAdicionales);
            }
            else
            {
                c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getDescripcion(),FontFactory.getFont("arial", 8)));
            }
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioUnitario(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getDescuento(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioTotalSinImpuesto(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
        }
        
        String baseImponibleIVA12 = "0.00";
        String valorIVA12 = "0.00";
        String baseImponibleIVA0 = "0.00";
        String baseImponibleNoIVA = "0.00";
        String valorICE = "0.00";
        
        for(int i = 0; i < factura.getTotalConImpuestos().getImpuesto().size(); i++)
        {
            if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (factura.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("0")))
            {
                baseImponibleIVA0 = factura.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
            }
            else if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (factura.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("3")))
            {
                baseImponibleIVA12 = factura.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
                valorIVA12 = factura.getTotalConImpuestos().getImpuesto().get(i).getValor();
            }
            else if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (factura.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("6")))
            {
                baseImponibleNoIVA = factura.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
            }
            else if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("3")))
            {
                valorICE = factura.getTotalConImpuestos().getImpuesto().get(i).getValor();
            }
        }
        
        PdfPTable Seccion4 = new PdfPTable(3);   
        Seccion4.getDefaultCell().setBorder(0);
        Seccion4.getDefaultCell().setPadding(0);
        
        PdfPTable infoAdicional = new PdfPTable(1);
        infoAdicional.getDefaultCell().setBorder(0);
        infoAdicional.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        infoAdicional.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        c1 = new PdfPCell(new Phrase("Informacion Adicional:",FontFactory.getFont("arial", 8,Font.BOLD)));
        c1.setBorderWidthBottom(0);
        infoAdicional.addCell(c1);
        for(int i = 0 ; i < factura.getInfoAdicional().getCamposAdicionales().size() ; i++)
        {
            c1 = new PdfPCell(new Phrase(factura.getInfoAdicional().getCamposAdicionales().get(i).getNombre() + ": " + factura.getInfoAdicional().getCamposAdicionales().get(i).getValor(),FontFactory.getFont("arial", 8)));
            c1.setBorderWidthTop(0);
            c1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            if(i != factura.getInfoAdicional().getCamposAdicionales().size()-1)
            {
                c1.setBorderWidthBottom(0);
            }
            infoAdicional.addCell(c1);
        }
        PdfPTable totales = new PdfPTable(2);
        totales.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        totales.addCell(new Phrase("SUBTOTAL 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL 0%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA0,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL NO OBJETO IVA",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleNoIVA,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL SIN IMPUESTOS",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(factura.getTotalSinImpuestos(),FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("DESCUENTO",FontFactory.getFont("arial", 8)));
        if(Double.parseDouble(factura.getTotalDescuento())>0)
        totales.addCell(new Phrase(factura.getTotalDescuento(),FontFactory.getFont("arial", 8)));
        else
          totales.addCell(new Phrase("0.00",FontFactory.getFont("arial", 8)));  
        totales.addCell(new Phrase("ICE",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorICE,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("IVA 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("PROPINA",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(factura.getPropina()+"0",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("VALOR TOTAL",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(factura.getImporteTotal(),FontFactory.getFont("arial", 8)));
        
        //totales.setWidths(new float[]{30,15});
        totales.setWidths(new float[]{22,13});
        Seccion4.addCell(infoAdicional);
        Seccion4.addCell(new Phrase(" "));
        Seccion4.addCell(totales);
        
        PdfPTable Seccion1 = getSeccionCabecera(factura, imagen, fechaDeAutorizacion, NumeroDeAutorizacion, writer);
        p.add(Seccion1);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion2);

//------------------------Seccion Exportacion INCIO -------------------------

        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        
        PdfPTable seccionExportacion = new PdfPTable(1);     
        seccionExportacion.getDefaultCell().setPadding(3);
        PdfPTable DatosExportacion = new PdfPTable(4);        
        DatosExportacion.getDefaultCell().setBorder(0);
        DatosExportacion.addCell(new Phrase("Factura:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosExportacion.addCell(new Phrase(factura.getComercioExterior(),FontFactory.getFont("arial", 8)));
        DatosExportacion.addCell(new Phrase("IncoTermFactura:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosExportacion.addCell(new Phrase(factura.getIncoTermFactura(),FontFactory.getFont("arial", 8)));
        DatosExportacion.addCell(new Phrase("Puerto Embarque:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosExportacion.addCell(new Phrase(factura.getPuertoEmbarque(),FontFactory.getFont("arial", 8)));
        DatosExportacion.addCell(new Phrase("Puerto Destino:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosExportacion.addCell(new Phrase(factura.getPuertoDestino(),FontFactory.getFont("arial", 8)));
        DatosExportacion.setWidths(new float[]{25,25,25,25});
        seccionExportacion.addCell(DatosExportacion);
        seccionExportacion.setWidthPercentage(100);
        p.add(seccionExportacion);
        
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        
        PdfPTable seccionPagos = new PdfPTable(4);
        seccionPagos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        seccionPagos.addCell(new Phrase("Forma Pago",FontFactory.getFont("arial", 8,Font.BOLD)));
        seccionPagos.addCell(new Phrase("Total",FontFactory.getFont("arial", 8,Font.BOLD)));
        seccionPagos.addCell(new Phrase("Plazo",FontFactory.getFont("arial", 8,Font.BOLD)));
        seccionPagos.addCell(new Phrase("Unidad Tiempo",FontFactory.getFont("arial", 8,Font.BOLD)));
        for(int i = 0 ; i < factura.getPagosFactura().getPagoFactura().size(); i++)
        {
            c1 = new PdfPCell(new Phrase(org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla24.getLabel(factura.getPagosFactura().getPagoFactura().get(i).getFormaPago()),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            seccionPagos.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getPagosFactura().getPagoFactura().get(i).getTotal(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            seccionPagos.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getPagosFactura().getPagoFactura().get(i).getPlazo(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            seccionPagos.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getPagosFactura().getPagoFactura().get(i).getUnidadTiempo(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            seccionPagos.addCell(c1);
        }
        seccionPagos.setWidthPercentage(100);
        seccionPagos.setWidths(new float[]{25,25,25,25});
        p.add(seccionPagos);
        
//------------------------Seccion Exportacion FIN -------------------------
        
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion3);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 1)));
        p.add(Seccion4);
        
        Seccion4.setWidthPercentage(100);
        Seccion4.setWidths(new float[]{62,3,35});
        Seccion3.setWidthPercentage(100);
        Seccion3.setWidths(new float[]{13,10,42,13,9,13});
        Seccion2.setWidthPercentage(100);
        Seccion1.setWidthPercentage(100);
        Seccion1.setWidths(new float[]{42,2,56});
        
        document.add(p);
        document.close();

        return baos.toByteArray();
    }
    
    public byte [] getFactura(Factura factura,XMLGregorianCalendar fechaDeAutorizacion, String NumeroDeAutorizacion, byte [] imagen, byte [] logoSub) throws DocumentException, FileNotFoundException, BadElementException, IOException
    {
        PdfPCell c1 = null;
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A4,36,36,36,48);
        
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        HeaderFooter agregaPieDePagina = new HeaderFooter();
        writer.setPageEvent(agregaPieDePagina);
        
        document.open();
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
        
        //Seccion1
                
        PdfPTable Seccion2 = new PdfPTable(1);     
        Seccion2.getDefaultCell().setPadding(3);
        
        PdfPTable DatosCliente = new PdfPTable(4);        
        DatosCliente.getDefaultCell().setBorder(0);
        DatosCliente.addCell(new Phrase("Razón Social / Nombres ",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getRazonSocialDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("RUC / CI:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getIdentificacionDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("Fecha Emisión:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getFechaEmision(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("Guía Remisión:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(factura.getGuiaRemision(),FontFactory.getFont("arial", 8)));
        
        DatosCliente.setWidths(new float[]{28,43,14,15});
        Seccion2.addCell(DatosCliente);
        
        PdfPTable Seccion3 = new PdfPTable(8);
        Seccion3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
        Seccion3.addCell(new Phrase("Cod.\nPrincipal",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Cantidad",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Descripción",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Precio\nUnitario",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Subsidio",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Precio Sin\nSubsidio",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Descuento",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Precio\nTotal",FontFactory.getFont("arial", 8,Font.BOLD)));
        
        for(int i = 0 ; i < factura.getDetallesFactura().getDetalleFactura().size(); i++)
        {
            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getCodigoPrincipal(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getCantidad(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getDescripcion(),FontFactory.getFont("arial", 8)));
            
            if(!factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().isEmpty())
            {
                PdfPTable detallesAdicionales = new PdfPTable(2);
                detallesAdicionales.getDefaultCell().setBorder(0);
                detallesAdicionales.setWidthPercentage(100);
                detallesAdicionales.setWidths(new float[]{50,50});
                
                PdfPCell cellSpan = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getDescripcion(),FontFactory.getFont("arial", 8)));
                cellSpan.setColspan(2);
                cellSpan.setBorder(0);
                detallesAdicionales.addCell(cellSpan);
                detallesAdicionales.completeRow();
                
                for(int j = 0; j< factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().size();j++)
                {
                    
                    String tmp = factura.getDetallesFactura().getDetalleFactura().get(i).getDetallesAdicionales().getDetalleAdicional().get(j).getValor();
                    String detallesAdicionalesArray [] = tmp.split(";");
                    if(detallesAdicionalesArray != null)
                    {
                        for(int k = 0 ; k < detallesAdicionalesArray.length ; k++)
                        {
                            if(detallesAdicionalesArray[k].length() != 0)
                            {
                                String detallesAdicionalesArray1 [] = detallesAdicionalesArray[k].split("=");
                                if(detallesAdicionalesArray1 != null)
                                {
                                    if(detallesAdicionalesArray1.length == 2)
                                    {
                                        PdfPCell cellPadding = new PdfPCell(new Phrase(detallesAdicionalesArray1[0],FontFactory.getFont("arial", 6)));
                                        cellPadding.setBorder(0);
                                        cellPadding.setPaddingLeft(25);
                                        detallesAdicionales.addCell(cellPadding);
                                        detallesAdicionales.addCell(new Phrase(detallesAdicionalesArray1[1],FontFactory.getFont("arial", 6)));
                                    }
                                }
                            }
                        }
                    }
                }
                c1.addElement(detallesAdicionales);
            }
            
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioUnitario(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
            ////zona subsidios
            double precioUnitario=Double.parseDouble(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioUnitario());
            double cantidad=Double.parseDouble(factura.getDetallesFactura().getDetalleFactura().get(i).getCantidad());
            double descuento=Double.parseDouble(factura.getDetallesFactura().getDetalleFactura().get(i).getDescuento());
            double precioTotalSinSubsidio=0;
            double subsidio;
            if(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio()!=null){
            precioTotalSinSubsidio=Double.parseDouble(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioSinSubsidio());
            subsidio=new BigDecimal("" + (precioTotalSinSubsidio-((precioUnitario*cantidad)-descuento))/cantidad).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }else{
            subsidio=0;
               precioTotalSinSubsidio=  ((precioUnitario*cantidad)-descuento)/cantidad;              
            }
           
            c1 = new PdfPCell(new Phrase((subsidio)+"",FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(""+precioTotalSinSubsidio,FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
            ///fin zona subsidios
            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getDescuento(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(factura.getDetallesFactura().getDetalleFactura().get(i).getPrecioTotalSinImpuesto(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
        }
        
        String baseImponibleIVA12 = "0.00";
        String valorIVA12 = "0.00";
        String baseImponibleIVA0 = "0.00";
        String baseImponibleNoIVA = "0.00";
        String valorICE = "0.00";
        
        for(int i = 0; i < factura.getTotalConImpuestos().getImpuesto().size(); i++)
        {
            if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (factura.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("0")))
            {
                baseImponibleIVA0 = factura.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
            }
            else if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (factura.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("4")))
            {
                baseImponibleIVA12 = factura.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
                valorIVA12 = factura.getTotalConImpuestos().getImpuesto().get(i).getValor();
            }
            else if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (factura.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("6")))
            {
                baseImponibleNoIVA = factura.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
            }
            else if((factura.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("3")))
            {
                valorICE = factura.getTotalConImpuestos().getImpuesto().get(i).getValor();
            }
        }
        
        PdfPTable Seccion4 = new PdfPTable(3);   
        Seccion4.getDefaultCell().setBorder(0);
        Seccion4.getDefaultCell().setPadding(0);
        
        PdfPTable infoAdicional = new PdfPTable(1);
        infoAdicional.getDefaultCell().setBorder(0);
        infoAdicional.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        infoAdicional.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        c1 = new PdfPCell(new Phrase("Informacion Adicional:",FontFactory.getFont("arial", 8,Font.BOLD)));
        c1.setBorderWidthBottom(0);
        infoAdicional.addCell(c1);
        for(int i = 0 ; i < factura.getInfoAdicional().getCamposAdicionales().size() ; i++)
        {
            c1 = new PdfPCell(new Phrase(factura.getInfoAdicional().getCamposAdicionales().get(i).getNombre() + ": " + factura.getInfoAdicional().getCamposAdicionales().get(i).getValor(),FontFactory.getFont("arial", 8)));
            c1.setBorderWidthTop(0);
            c1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            if(i != factura.getInfoAdicional().getCamposAdicionales().size()-1)
            {
                c1.setBorderWidthBottom(0);
            }
            
            if(i<=5)
            infoAdicional.addCell(c1);
        }
        PdfPTable totales = new PdfPTable(2);
        totales.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        totales.addCell(new Phrase("SUBTOTAL 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL 0%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA0,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL NO OBJETO IVA",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleNoIVA,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL SIN IMPUESTOS",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(factura.getTotalSinImpuestos(),FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("DESCUENTO",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(factura.getTotalDescuento(),FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("ICE",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorICE,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("IVA 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("PROPINA",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(factura.getPropina()+"0",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("VALOR TOTAL",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(factura.getImporteTotal(),FontFactory.getFont("arial", 8)));
        
        totales.setWidths(new float[]{22,13});
        Seccion4.addCell(infoAdicional);
        Seccion4.addCell(new Phrase(" "));
        Seccion4.addCell(totales);
        
        PdfPTable subsidios = new PdfPTable(2);
        subsidios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        subsidios.addCell(new Phrase("\nVALOR TOTAL SIN SUBSIDIO\n",FontFactory.getFont("arial", 8)));
        double total=new BigDecimal("" + (Double.parseDouble(factura.getTotalSubsidio())+Double.parseDouble(factura.getImporteTotal()))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        subsidios.addCell(new Phrase(""+(total),FontFactory.getFont("arial", 8)));
        subsidios.addCell(new Phrase("\nAHORRO POR SUBSIDIO\n(Incluye IVA cuando aplique)\n",FontFactory.getFont("arial", 8)));
        subsidios.addCell(new Phrase(factura.getTotalSubsidio(),FontFactory.getFont("arial", 8)));
        
        subsidios.setWidths(new float[]{22,13});
        PdfPTable Seccion5Subsidios = new PdfPTable(3);   
        Seccion5Subsidios.getDefaultCell().setBorder(0);
        Seccion5Subsidios.getDefaultCell().setPadding(0);
        Image im = Image.getInstance(logoSub);
        PdfPCell imgCell = new PdfPCell(im);
                 imgCell.setFixedHeight(74);
                 imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);   
                 imgCell.setBorder(0);
            Seccion5Subsidios.addCell(imgCell);
        Seccion5Subsidios.addCell(new Phrase(" "));
        Seccion5Subsidios.addCell(subsidios);
        
        //formas de pago
        PdfPTable Seccion6pagos = new PdfPTable(2);
        Seccion6pagos.setWidths(new float[]{30,4});
        Seccion6pagos.getDefaultCell().setBorder(0);
        Seccion6pagos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        Seccion6pagos.addCell(new PdfPCell(new Phrase("Forma De Pago",FontFactory.getFont("arial", 8,Font.BOLD)))); 
        Seccion6pagos.addCell(new PdfPCell(new Phrase("Valor",FontFactory.getFont("arial", 8,Font.BOLD)))); 
        for(int i = 0 ; i < factura.getPagos().getPago().size() ; i++)
        {
            PdfPCell pag1 = new PdfPCell(new Phrase(getDescripcionPago(factura.getPagos().getPago().get(i).getFormaPago()),FontFactory.getFont("arial", 8))); 
            PdfPCell pag2 = new PdfPCell(new Phrase(factura.getPagos().getPago().get(i).getTotal(),FontFactory.getFont("arial", 8)));
            pag2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            pag1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            Seccion6pagos.addCell(pag1);
            Seccion6pagos.addCell(pag2);
        }
        //formas de pago
        
        PdfPTable Seccion1 = getSeccionCabecera(factura, imagen, fechaDeAutorizacion, NumeroDeAutorizacion, writer);
        
        p.add(Seccion1);
        p.add(new Paragraph(" ", FontFactory.getFont("arial", 6)));
        if (factura.getAguaDatoAdicional() != null) {
            p.add(createTableDatoAdicionalAgua(factura.getAguaDatoAdicional()));
            p.add(new Paragraph(" ", FontFactory.getFont("arial", 6)));
        }
        p.add(Seccion2);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion3);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 1)));
        p.add(Seccion4);
        if (Double.parseDouble(factura.getTotalSubsidio()) > 0) {
            p.add(new Paragraph(" ", FontFactory.getFont("arial", 4)));
            p.add(Seccion5Subsidios);
        }     
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 4)));
        p.add(Seccion6pagos);
        if (factura.getAguaDatoAdicional() != null) {
            p.add(new Paragraph(" ", FontFactory.getFont("arial", 6)));
            p.add(createTableImagenDatoAdicionalAgua(factura.getAguaDatoAdicional()));
            p.add(new Paragraph(" ", FontFactory.getFont("arial", 6)));
        }
    
        Seccion5Subsidios.setWidthPercentage(100);
        Seccion5Subsidios.setWidths(new float[]{62,3,35});
        Seccion4.setWidthPercentage(100);
        Seccion4.setWidths(new float[]{62,3,35});
        Seccion3.setWidthPercentage(100);
        Seccion3.setWidths(new float[]{8,8,36,7,9,9,9,13});//+4+2+2+4+//1+1+2+2
        Seccion2.setWidthPercentage(100);
        Seccion1.setWidthPercentage(100);
        Seccion1.setWidths(new float[]{42,2,56});
        
        document.add(p);
        document.close();

        return baos.toByteArray();
    }
    
    public String getDescripcionPago(String idFormaPagoSRI){
            switch (idFormaPagoSRI) {
                case "01":
                    return "SIN UTILIZACION DEL SISTEMA FINANCIERO";
                case "15":
                    return "COMPENSACION DE DEUDAS ";
                case "16":
                    return "TARJETA DE DEBITO";
                case "17":
                    return "DINERO ELECTRONICO";
                case "18":
                    return "TARJETA PREPAGO";
                case "19":
                    return "TARJETA DE CREDITO";
                case "20":
                    return "OTROS CON UTILIZACION DEL SISTEMA FINANCIERO";
                case "21":
                    return "ENDOSO DE TITULOS";
            }
        return  "SIN UTILIZACION DEL SISTEMA FINANCIERO";
    }
        
    public byte [] getRetencion(Retencion retencion,XMLGregorianCalendar fechaDeAutorizacion, String NumeroDeAutorizacion, byte [] imagen) throws DocumentException, FileNotFoundException, BadElementException, IOException
    {
        PdfPCell c1 = null;
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A4,36,36,36,48);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        HeaderFooter agregaPieDePagina = new HeaderFooter();
        writer.setPageEvent(agregaPieDePagina);
        
        document.open();
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
        
        //Seccion1
                
        PdfPTable Seccion2 = new PdfPTable(1);     
        Seccion2.getDefaultCell().setPadding(3);
        
        PdfPTable DatosCliente = new PdfPTable(4);        
        DatosCliente.getDefaultCell().setBorder(0);
        DatosCliente.addCell(new Phrase("Razón Social / Nombres y Apellidos:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(retencion.getRazonSocialSujetoRetenido(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("RUC / CI:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(retencion.getIdentificacionSujetoRetenido(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("Fecha Emisión:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(retencion.getFechaEmision(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase(" ",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        
        DatosCliente.setWidths(new float[]{28,43,14,15});
        Seccion2.addCell(DatosCliente);
        
        PdfPTable Seccion3 = new PdfPTable(8);      
        Seccion3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
        Seccion3.addCell(new Phrase("Comprobante",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Numero",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Fecha Emision",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Ejercicio Fiscal",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Base Imponible para la Retencion",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("IMPUESTO",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Porcentaje Retencion",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Valor Retenido",FontFactory.getFont("arial", 8,Font.BOLD)));
        
        for(int i = 0 ; i < retencion.getImpuestosRetencion().getImpuestoRetencion().size(); i++)
        {
        
            c1 = new PdfPCell(new Phrase(org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getCodDocSustento()),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getNumDocSustento(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getFechaEmisionDocSustento(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(retencion.getPeriodoFiscal(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getBaseImponible(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla17.getLabel(retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getCodigo()),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getPorcentajeRetener(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(retencion.getImpuestosRetencion().getImpuestoRetencion().get(i).getValorRetenido(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);        
            Seccion3.addCell(c1);
        }
        
        PdfPTable Seccion4 = new PdfPTable(3);   
        Seccion4.getDefaultCell().setBorder(0);
        Seccion4.getDefaultCell().setPadding(0);
        
        PdfPTable infoAdicional = new PdfPTable(1);
        infoAdicional.getDefaultCell().setBorder(0);
        infoAdicional.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        infoAdicional.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        c1 = new PdfPCell(new Phrase("Informacion Adicional:",FontFactory.getFont("arial", 8,Font.BOLD)));
        c1.setBorderWidthBottom(0);
        infoAdicional.addCell(c1);
        for(int i = 0 ; i < retencion.getInfoAdicional().getCamposAdicionales().size() ; i++)
        {
            c1 = new PdfPCell(new Phrase(retencion.getInfoAdicional().getCamposAdicionales().get(i).getNombre() + ": " + retencion.getInfoAdicional().getCamposAdicionales().get(i).getValor(),FontFactory.getFont("arial", 8)));
            c1.setBorderWidthTop(0);
            c1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            if(i != retencion.getInfoAdicional().getCamposAdicionales().size()-1)
            {
                c1.setBorderWidthBottom(0);
            }
            infoAdicional.addCell(c1);
        }

        Seccion4.addCell(infoAdicional);
        Seccion4.addCell(new Phrase(" "));
        Seccion4.addCell(new Phrase(" "));
        
        PdfPTable Seccion1 = getSeccionCabecera(retencion, imagen, fechaDeAutorizacion, NumeroDeAutorizacion, writer);
        
        p.add(Seccion1);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion2);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion3);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 1)));
        p.add(Seccion4);
        
        Seccion4.setWidthPercentage(100);
        Seccion4.setWidths(new float[]{52,3,45});
        Seccion3.setWidthPercentage(100);
        Seccion3.setWidths(new float[]{18f,16f,10f,10f,12.5f,10f,11f,12.5f});
        Seccion2.setWidthPercentage(100);
        Seccion1.setWidthPercentage(100);
        Seccion1.setWidths(new float[]{42,2,56});
        
        document.add(p);
        document.close();

        return baos.toByteArray();
    }
    
    public byte [] getCredito(Credito credito,XMLGregorianCalendar fechaDeAutorizacion, String NumeroDeAutorizacion, byte [] imagen) throws DocumentException, FileNotFoundException, BadElementException, IOException
    {
        PdfPCell c1 = null;
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A4,36,36,36,48);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        HeaderFooter agregaPieDePagina = new HeaderFooter();
        writer.setPageEvent(agregaPieDePagina);
        
        document.open();
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
        
        //Seccion1
                
        PdfPTable Seccion2 = new PdfPTable(1);
        
        PdfPTable Seccion2_1 = new PdfPTable(1); 
        Seccion2_1.getDefaultCell().setBorder(0);
        
        PdfPTable DatosCliente = new PdfPTable(4);        
        DatosCliente.getDefaultCell().setBorder(0);
        DatosCliente.addCell(new Phrase("Razón Social / Nombres y Apellidos:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(credito.getRazonSocialDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("RUC / CI:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(credito.getIdentificacionDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("Fecha Emisión:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(credito.getFechaEmision(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase(" ",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        DatosCliente.setWidths(new float[]{28,43,14,15});
        
        PdfPTable DatosNotaCredito = new PdfPTable(3);
        DatosNotaCredito.getDefaultCell().setBorder(0);
        DatosNotaCredito.addCell(new Phrase("Comprobante que se modifica:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosNotaCredito.addCell(new Phrase(org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(credito.getCodDocModificado()),FontFactory.getFont("arial", 8)));
        DatosNotaCredito.addCell(new Phrase(credito.getNumDocModificado(),FontFactory.getFont("arial", 8)));
        DatosNotaCredito.addCell(new Phrase("Fecha Emision (Comprobante a modificar):",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosNotaCredito.addCell(new Phrase(credito.getFechaEmisionDocSustento(),FontFactory.getFont("arial", 8)));
        DatosNotaCredito.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        DatosNotaCredito.addCell(new Phrase("Razon de Modificacion:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosNotaCredito.addCell(new Phrase(credito.getMotivo(),FontFactory.getFont("arial", 8)));
        DatosNotaCredito.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        DatosNotaCredito.setWidths(new float[]{50,25,25});
        
        Seccion2_1.addCell(DatosCliente);
        Phrase line = new Phrase();
        line.add(new LineSeparator(0.4f, 90f, null, Element.ALIGN_CENTER, 0));
        Seccion2_1.addCell(line);
        Seccion2_1.addCell(DatosNotaCredito);
        
        Seccion2.addCell(Seccion2_1);
        
        PdfPTable Seccion3 = new PdfPTable(6);      
        Seccion3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
        Seccion3.addCell(new Phrase("Cod. Principal",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Cantidad",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Descripción",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Descuento",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Precio Unitario",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("Precio Total",FontFactory.getFont("arial", 8,Font.BOLD)));
        
        for(int i = 0 ; i < credito.getDetallesCredito().getDetalleCredito().size(); i++)
        {
        
            c1 = new PdfPCell(new Phrase(credito.getDetallesCredito().getDetalleCredito().get(i).getCodigoInterno(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(credito.getDetallesCredito().getDetalleCredito().get(i).getCantidad(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(credito.getDetallesCredito().getDetalleCredito().get(i).getDescripcion(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(credito.getDetallesCredito().getDetalleCredito().get(i).getDescuento(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(credito.getDetallesCredito().getDetalleCredito().get(i).getPrecioUnitario(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(credito.getDetallesCredito().getDetalleCredito().get(i).getPrecioTotalSinImpuesto(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
        }
        
        String baseImponibleIVA12 = "0.00";
        String valorIVA12 = "0.00";
        String baseImponibleIVA0 = "0.00";
        String baseImponibleNoIVA = "0.00";
        String valorICE = "0.00";
        
        for(int i = 0; i < credito.getTotalConImpuestos().getImpuesto().size(); i++)
        {
            if((credito.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (credito.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("0")))
            {
                baseImponibleIVA0 = credito.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
            }
            else if((credito.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (credito.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("4")))
            {
                baseImponibleIVA12 = credito.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
                valorIVA12 = credito.getTotalConImpuestos().getImpuesto().get(i).getValor();
            }
            else if((credito.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("2")) & (credito.getTotalConImpuestos().getImpuesto().get(i).getCodigoPorcentaje().equals("6")))
            {
                baseImponibleNoIVA = credito.getTotalConImpuestos().getImpuesto().get(i).getBaseImponible();
            }
            else if((credito.getTotalConImpuestos().getImpuesto().get(i).getCodigo().equals("3")))
            {
                valorICE = credito.getTotalConImpuestos().getImpuesto().get(i).getValor();
            }
        }
        
        PdfPTable Seccion4 = new PdfPTable(3);   
        Seccion4.getDefaultCell().setBorder(0);
        Seccion4.getDefaultCell().setPadding(0);
        
        PdfPTable infoAdicional = new PdfPTable(1);
        infoAdicional.getDefaultCell().setBorder(0);
        infoAdicional.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        infoAdicional.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        c1 = new PdfPCell(new Phrase("Informacion Adicional:",FontFactory.getFont("arial", 8,Font.BOLD)));
        c1.setBorderWidthBottom(0);
        infoAdicional.addCell(c1);
        
        for(int i = 0 ; i < credito.getInfoAdicional().getCamposAdicionales().size() ; i++)
        {
            c1 = new PdfPCell(new Phrase(credito.getInfoAdicional().getCamposAdicionales().get(i).getNombre() + ": " + credito.getInfoAdicional().getCamposAdicionales().get(i).getValor(),FontFactory.getFont("arial", 8)));
            c1.setBorderWidthTop(0);
            c1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            if(i != credito.getInfoAdicional().getCamposAdicionales().size()-1)
            {
                c1.setBorderWidthBottom(0);
            }
            infoAdicional.addCell(c1);
        }
        
        PdfPTable totales = new PdfPTable(2);
        totales.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        totales.addCell(new Phrase("SUBTOTAL 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL 0%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA0,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL No Objeto IVA",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleNoIVA,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL SIN IMPUESTOS",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(credito.getTotalSinImpuestos(),FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("DESCUENTO",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("0.00",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("ICE",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorICE,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("IVA 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("VALOR TOTAL",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(credito.getValorModificacion(),FontFactory.getFont("arial", 8)));
        
        totales.setWidths(new float[]{30,15});
        //c1 = new PdfPCell();
        Seccion4.addCell(infoAdicional);
        Seccion4.addCell(new Phrase(" "));
        Seccion4.addCell(totales);
        
        PdfPTable Seccion1 = getSeccionCabecera(credito, imagen, fechaDeAutorizacion, NumeroDeAutorizacion, writer);
        
        p.add(Seccion1);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion2);
        p.add(Seccion3);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 1)));
        p.add(Seccion4);
        
        Seccion4.setWidthPercentage(100);
        Seccion4.setWidths(new float[]{52,3,45});
        Seccion3.setWidthPercentage(100);
        Seccion3.setWidths(new float[]{12,10,33,15,15,15});
        Seccion2.setWidthPercentage(100);
        Seccion1.setWidthPercentage(100);
        Seccion1.setWidths(new float[]{42,2,56});
        
        document.add(p);
        document.close();

        return baos.toByteArray();
    }    
    
    public byte [] getDebito(Debito debito,XMLGregorianCalendar fechaDeAutorizacion, String NumeroDeAutorizacion, byte [] imagen) throws DocumentException, FileNotFoundException, BadElementException, IOException
    {
        PdfPCell c1 = null;
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A4,36,36,36,48);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        HeaderFooter agregaPieDePagina = new HeaderFooter();
        writer.setPageEvent(agregaPieDePagina);
        
        document.open();
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
        
        //Seccion1
                
        PdfPTable Seccion2 = new PdfPTable(1);
        
        PdfPTable Seccion2_1 = new PdfPTable(1); 
        Seccion2_1.getDefaultCell().setBorder(0);
        
        PdfPTable DatosCliente = new PdfPTable(4);        
        DatosCliente.getDefaultCell().setBorder(0);
        DatosCliente.addCell(new Phrase("Razón Social / Nombres y Apellidos:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(debito.getRazonSocialDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("RUC / CI:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(debito.getIdentificacionDelComprador(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase("Fecha Emisión:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(debito.getFechaEmision(),FontFactory.getFont("arial", 8)));
        DatosCliente.addCell(new Phrase(" ",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosCliente.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        DatosCliente.setWidths(new float[]{28,43,14,15});
        
        PdfPTable DatosNotaDebito = new PdfPTable(3);
        DatosNotaDebito.getDefaultCell().setBorder(0);
        DatosNotaDebito.addCell(new Phrase("Comprobante que se modifica:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosNotaDebito.addCell(new Phrase(org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(debito.getCodDocModificado()),FontFactory.getFont("arial", 8)));
        DatosNotaDebito.addCell(new Phrase(debito.getNumDocModificado(),FontFactory.getFont("arial", 8)));
        DatosNotaDebito.addCell(new Phrase("Fecha Emision (Comprobante a modificar):",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosNotaDebito.addCell(new Phrase(debito.getFechaEmisionDocSustento(),FontFactory.getFont("arial", 8)));
        DatosNotaDebito.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        DatosNotaDebito.setWidths(new float[]{50,25,25});
        
        Seccion2_1.addCell(DatosCliente);
        Phrase line = new Phrase();
        line.add(new LineSeparator(0.4f, 90f, null, Element.ALIGN_CENTER, 0));
        Seccion2_1.addCell(line);
        Seccion2_1.addCell(DatosNotaDebito);
        
        Seccion2.addCell(Seccion2_1);
        
        PdfPTable Seccion3 = new PdfPTable(2);      
        Seccion3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
        Seccion3.addCell(new Phrase("RAZON DE LA MODIFICACION",FontFactory.getFont("arial", 8,Font.BOLD)));
        Seccion3.addCell(new Phrase("VALOR DE LA MODIFICACION",FontFactory.getFont("arial", 8,Font.BOLD)));
        
        for(int i = 0 ; i < debito.getMotivosDebito().getMotivoDebito().size(); i++)
        {
        
            c1 = new PdfPCell(new Phrase(debito.getMotivosDebito().getMotivoDebito().get(i).getRazon(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
            Seccion3.addCell(c1);

            c1 = new PdfPCell(new Phrase(debito.getMotivosDebito().getMotivoDebito().get(i).getValor(),FontFactory.getFont("arial", 8)));
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);        
            Seccion3.addCell(c1);
        }
        
        String baseImponibleIVA12 = "0.00";
        String valorIVA12 = "0.00";
        String baseImponibleIVA0 = "0.00";
        String baseImponibleNoIVA = "0.00";
        String valorICE = "0.00";
        
        for(int i = 0; i < debito.getDebitoImpuestos().getDebitoImpuesto().size(); i++)
        {
            if((debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigo().equals("2")) & (debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigoPorcentaje().equals("0")))
            {
                baseImponibleIVA0 = debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getBaseImponible();
            }
            else if((debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigo().equals("2")) & (debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigoPorcentaje().equals("4")))
            {
                baseImponibleIVA12 = debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getBaseImponible();
                valorIVA12 = debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getValor();
            }
            else if((debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigo().equals("2")) & (debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigoPorcentaje().equals("6")))
            {
                baseImponibleNoIVA = debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getBaseImponible();
            }
            else if((debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getCodigo().equals("3")))
            {
                valorICE = debito.getDebitoImpuestos().getDebitoImpuesto().get(i).getValor();
            }
        }
        
        PdfPTable Seccion4 = new PdfPTable(3);   
        Seccion4.getDefaultCell().setBorder(0);
        Seccion4.getDefaultCell().setPadding(0);
        
        PdfPTable infoAdicional = new PdfPTable(1);
        infoAdicional.getDefaultCell().setBorder(0);
        infoAdicional.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        infoAdicional.addCell(new Phrase(" ",FontFactory.getFont("arial", 8)));
        c1 = new PdfPCell(new Phrase("Informacion Adicional:",FontFactory.getFont("arial", 8,Font.BOLD)));
        c1.setBorderWidthBottom(0);
        infoAdicional.addCell(c1);
        
        for(int i = 0 ; i < debito.getInfoAdicional().getCamposAdicionales().size() ; i++)
        {
            c1 = new PdfPCell(new Phrase(debito.getInfoAdicional().getCamposAdicionales().get(i).getNombre() + ": " + debito.getInfoAdicional().getCamposAdicionales().get(i).getValor(),FontFactory.getFont("arial", 8)));
            c1.setBorderWidthTop(0);
            c1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            if(i != debito.getInfoAdicional().getCamposAdicionales().size()-1)
            {
                c1.setBorderWidthBottom(0);
            }
            infoAdicional.addCell(c1);
        }
        
        PdfPTable totales = new PdfPTable(2);
        totales.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        totales.addCell(new Phrase("SUBTOTAL 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL 0%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleIVA0,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL No Objeto IVA",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(baseImponibleNoIVA,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("SUBTOTAL SIN IMPUESTOS",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(debito.getTotalSinImpuestos(),FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("VALOR ICE",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorICE,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("IVA 15%",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(valorIVA12,FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase("VALOR TOTAL",FontFactory.getFont("arial", 8)));
        totales.addCell(new Phrase(debito.getValorTotal(),FontFactory.getFont("arial", 8)));
        
        totales.setWidths(new float[]{30,15});
        //c1 = new PdfPCell();
        Seccion4.addCell(infoAdicional);
        Seccion4.addCell(new Phrase(" "));
        Seccion4.addCell(totales);
        
        PdfPTable Seccion1 = getSeccionCabecera(debito, imagen, fechaDeAutorizacion, NumeroDeAutorizacion, writer);
        
        p.add(Seccion1);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion2);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion3);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 1)));
        p.add(Seccion4);
        
        Seccion4.setWidthPercentage(100);
        Seccion4.setWidths(new float[]{57,3,40});
        Seccion3.setWidthPercentage(100);
        Seccion3.setWidths(new float[]{60,40});
        Seccion2.setWidthPercentage(100);
        Seccion1.setWidthPercentage(100);
        Seccion1.setWidths(new float[]{42,2,56});
        
        document.add(p);
        document.close();

        return baos.toByteArray();
    }
    
    public byte [] getGuiaDeRemision(Remision remision,XMLGregorianCalendar fechaDeAutorizacion, String NumeroDeAutorizacion, byte [] imagen) throws DocumentException, FileNotFoundException, BadElementException, IOException
    {
        PdfPCell c1 = null;
        
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A4,36,36,36,48);
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        
        HeaderFooter agregaPieDePagina = new HeaderFooter();
        writer.setPageEvent(agregaPieDePagina);
        
        document.open();
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_RIGHT);
        
        //Seccion1
                
        PdfPTable Seccion2 = new PdfPTable(1);     
        Seccion2.getDefaultCell().setPadding(3);
        
        PdfPTable DatosTransporte = new PdfPTable(2);        
        DatosTransporte.getDefaultCell().setBorder(0);
        DatosTransporte.addCell(new Phrase("RUC / CI (Transportista):",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosTransporte.addCell(new Phrase(remision.getRucTransportista(),FontFactory.getFont("arial", 8)));
        DatosTransporte.addCell(new Phrase("Razón Social / Nombres y Apellidos:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosTransporte.addCell(new Phrase(remision.getRazonSocialTransportista(),FontFactory.getFont("arial", 8)));
        DatosTransporte.addCell(new Phrase("Placa:",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosTransporte.addCell(new Phrase(remision.getPlaca(),FontFactory.getFont("arial", 8)));
        DatosTransporte.addCell(new Phrase("Punto de Partida",FontFactory.getFont("arial", 8,Font.BOLD)));
        DatosTransporte.addCell(new Phrase(remision.getDirPartida(),FontFactory.getFont("arial", 8)));
        DatosTransporte.setWidths(new float[]{35,65});
        
        Seccion2.addCell(DatosTransporte);
        
        PdfPTable Seccion3 = new PdfPTable(1);      
        
        for(int j = 0 ; j < remision.getDestinatarios().getDestinatarioRemision().size(); j++)
        {
            PdfPTable Seccion3_1 = new PdfPTable(4);
            Seccion3_1.getDefaultCell().setBorder(0);

            Seccion3_1.addCell(new Phrase("Comprobante de Venta:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_1.addCell(new Phrase(org.inprofsolutions.sri.fe.comprobantesElectronicos.tablas.Tabla4.getLabel(remision.getDestinatarios().getDestinatarioRemision().get(j).getCodDocSustento()) +" "+ remision.getDestinatarios().getDestinatarioRemision().get(0).getNumDocSustento(),FontFactory.getFont("arial", 8)));
            Seccion3_1.addCell(new Phrase("Fecha de Emision:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_1.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getFechaEmisionDocSustento(),FontFactory.getFont("arial", 8)));
            Seccion3_1.addCell(new Phrase("Numero de Autorizacion:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_1.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getNumAutDocSustento(),FontFactory.getFont("arial", 8)));
            Seccion3_1.addCell(new Phrase(" ",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_1.addCell(new Phrase(" ",FontFactory.getFont("arial", 8,Font.BOLD)));

            PdfPTable Seccion3_2 = new PdfPTable(2);      
            Seccion3_2.getDefaultCell().setBorder(0);

            Seccion3_2.addCell(new Phrase("Motivo Traslado:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_2.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getMotivoTraslado(),FontFactory.getFont("arial", 8)));
            Seccion3_2.addCell(new Phrase("Destino (Punto de llegada):",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_2.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getDirDestinatario(),FontFactory.getFont("arial", 8)));
            Seccion3_2.addCell(new Phrase("RUC/CI (Destinatario):",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_2.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getIdentificacionDestinatario(),FontFactory.getFont("arial", 8)));
            Seccion3_2.addCell(new Phrase("Razón Social / Nombres y Apellidos:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_2.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getRazonSocialDestinatario(),FontFactory.getFont("arial", 8)));
            Seccion3_2.addCell(new Phrase("Documento Aduanero:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_2.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getDocAduaneroUnico(),FontFactory.getFont("arial", 8)));
            Seccion3_2.addCell(new Phrase("Codigo Establacimiento Destino:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_2.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getCodEstabDestino(),FontFactory.getFont("arial", 8)));
            Seccion3_2.addCell(new Phrase("Ruta:",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_2.addCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getRuta(),FontFactory.getFont("arial", 8)));

            PdfPTable Seccion3_3 = new PdfPTable(4);
            Seccion3_3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);        

            Seccion3_3.addCell(new Phrase("Cantidad",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_3.addCell(new Phrase("Descripcion",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_3.addCell(new Phrase("Codigo Principal",FontFactory.getFont("arial", 8,Font.BOLD)));
            Seccion3_3.addCell(new Phrase("Codigo Auxiliar",FontFactory.getFont("arial", 8,Font.BOLD)));

            for(int i = 0 ; i < remision.getDestinatarios().getDestinatarioRemision().get(j).getDetallesRemision().getDetalleRemision().size(); i++)
            {

                c1 = new PdfPCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getDetallesRemision().getDetalleRemision().get(i).getCantidad(),FontFactory.getFont("arial", 8)));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
                Seccion3_3.addCell(c1);

                c1 = new PdfPCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getDetallesRemision().getDetalleRemision().get(i).getDescripcion(),FontFactory.getFont("arial", 8)));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
                Seccion3_3.addCell(c1);

                c1 = new PdfPCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getDetallesRemision().getDetalleRemision().get(i).getCodigoInterno(),FontFactory.getFont("arial", 8)));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
                Seccion3_3.addCell(c1);

                c1 = new PdfPCell(new Phrase(remision.getDestinatarios().getDestinatarioRemision().get(j).getDetallesRemision().getDetalleRemision().get(i).getCodigoAdicional(),FontFactory.getFont("arial", 8)));
                c1.setHorizontalAlignment(Element.ALIGN_LEFT);        
                Seccion3_3.addCell(c1);
            }

            PdfPTable Seccion3_contenedor = new PdfPTable(1);      
            Seccion3_contenedor.getDefaultCell().setBorder(0);

            Seccion3_contenedor.addCell(Seccion3_1);
            Seccion3_contenedor.addCell(new Phrase(" "));
            Seccion3_contenedor.addCell(Seccion3_2);
            Seccion3_contenedor.addCell(new Phrase(" ",FontFactory.getFont("arial", 6)));
            c1 = new PdfPCell(Seccion3_3);
            c1.setBorder(0);
            c1.setPaddingLeft(20);
            c1.setPaddingRight(20);
            Seccion3_contenedor.addCell(c1);
            Seccion3_contenedor.addCell(new Phrase(" ",FontFactory.getFont("arial", 6)));

            Seccion3.addCell(Seccion3_contenedor);
            c1 = new PdfPCell(new Phrase(" ",FontFactory.getFont("arial", 6)));
            c1.setBorder(0);
            Seccion3.addCell(c1);
        }
        
        PdfPTable Seccion1 = getSeccionCabecera(remision, imagen, fechaDeAutorizacion, NumeroDeAutorizacion, writer);
        
        p.add(Seccion1);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion2);
        p.add(new Paragraph(" ",FontFactory.getFont("arial", 6)));
        p.add(Seccion3);

        Seccion3.setWidthPercentage(100);
        Seccion2.setWidthPercentage(100);
        Seccion1.setWidthPercentage(100);
        Seccion1.setWidths(new float[]{42,2,56});
        
        document.add(p);
        document.close();

        return baos.toByteArray();
    }

    public byte [] crearImagenConsumos(String nameSerie,List xSerieName,List ySerieValores){
    byte [] imagen=null;
    try{
        CategoryChart chart = new CategoryChartBuilder().width(400).height(200).title("Consumo").xAxisTitle("").yAxisTitle("Metros Cubicos (M3)").build();

        // Customize Chart
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setPlotContentSize(0.75);

        // Series, una serie es un grupo de barras por año o asi. 
        chart.addSeries(nameSerie,xSerieName, ySerieValores)
                .setFillColor(new Color(79,129,189));
        
         imagen=BitmapEncoder.getBitmapBytes(chart,BitmapEncoder.BitmapFormat.PNG);
         
    }catch(Exception e){e.printStackTrace();}
    
    return imagen;
    }
    
    public PdfPTable createTableDatoAdicionalAgua(AguaDatoAdicional ada) {
        PdfPTable table = new PdfPTable(6);
        table.getDefaultCell().setBorder(0);
        PdfPCell cellTitulo = new PdfPCell(new Phrase(ada.getEmision(), FontFactory.getFont("arial", 10, Font.BOLD)));
        cellTitulo.setColspan(6);
        cellTitulo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cellTitulo.setBorder(0);
        table.addCell(cellTitulo);

        table.addCell(new Phrase("Tarifa: ", FontFactory.getFont("arial", 9,Font.BOLD)));
        table.addCell(new Phrase(ada.getTarifaCategoria(), FontFactory.getFont("arial", 9)));
        table.addCell(new Phrase("Nro. Medidor: ", FontFactory.getFont("arial", 9,Font.BOLD)));
        table.addCell(new Phrase(ada.getNroMedidor(), FontFactory.getFont("arial", 9)));
        table.addCell(new Phrase("Novedad: ", FontFactory.getFont("arial", 9,Font.BOLD)));
        table.addCell(new Phrase(ada.getNovedad(), FontFactory.getFont("arial", 9)));

        table.addCell(new Phrase("Lec Anterior: ", FontFactory.getFont("arial", 9,Font.BOLD)));
        table.addCell(new Phrase(ada.getLecturaAnterior(), FontFactory.getFont("arial", 9)));
        table.addCell(new Phrase("Lec Actual: ", FontFactory.getFont("arial", 9,Font.BOLD)));
        table.addCell(new Phrase(ada.getLecturaActual(), FontFactory.getFont("arial", 9)));
        table.addCell(new Phrase("Consumo: ", FontFactory.getFont("arial", 9,Font.BOLD)));
        table.addCell(new Phrase(ada.getConsumo(), FontFactory.getFont("arial", 9)));

        PdfPTable Seccion1 = new PdfPTable(1);
        Seccion1.getDefaultCell().setBorder(0);
                        try{
          Seccion1.setWidthPercentage(100);
        }catch(Exception e){e.printStackTrace();}
        
        PdfPCell c1 = new PdfPCell(table);
        c1.setPaddingBottom(3);
        c1.setPaddingLeft(5);
        RoundRectangle r = new RoundRectangle(new int[]{0x00, 0x00, 0x00, 0xFF});
        c1.setCellEvent(r);
        c1.setBorder(0);
        Seccion1.addCell(c1);

        return Seccion1;
    }
    
    public PdfPTable createTableImagenDatoAdicionalAgua(AguaDatoAdicional ada){
        PdfPTable table = new PdfPTable(1);
            try {
            Image im = Image.getInstance(crearImagenConsumos(ada.getNameSerie(),ada.getxSerieName(),ada.getySerieValores()));
            PdfPCell imgCell = new PdfPCell(im);
            imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            imgCell.setBorder(0);
            imgCell.setColspan(3);
            imgCell.setFixedHeight(115);
            table.addCell(imgCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        return table;
    }
    
}