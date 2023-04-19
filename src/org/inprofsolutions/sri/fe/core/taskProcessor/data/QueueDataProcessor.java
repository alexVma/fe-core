/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.taskProcessor.data;

import java.io.File;
import org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.DefaultComprobanteElectronico;
import sri.gob.ec.client.ws.autorizacion.RespuestaComprobante;
import sri.gob.ec.client.ws.recepcion.RespuestaSolicitud;
import org.w3c.dom.Document;

/**
 *
 * @author caf
 */
public class QueueDataProcessor 
{
    public static String DOCUMENTO_DEVUELTA = "DEVUELTA";
    public static String DOCUMENTO_RECIBIDA = "RECIBIDA";
    public static String DOCUMENTO_NO_AUTORIZADO = "NO AUTORIZADO";
    public static String DOCUMENTO_AUTORIZADO = "AUTORIZADO";
    
    private DefaultComprobanteElectronico defaultComprobanteElectronico = null;
    private Document documentW3C = null;
    
    private RespuestaComprobante respuestaComprobante = null;
    private RespuestaSolicitud respuestaSolicitud = null;
    
    private String stringSigned = null;
    private byte [] pdfByte = null;
    
    private File fileCabecera = null; 
    private File fileDetalle = null;
    
    private String cabeceraArray [][] = null;
    private String detalleArray [][] = null;
    
    private String dbDocumentFilter [][] = null;
    private String estadoDocumento = null;
    
    private String tipoIntegracion = null;

    public void setDefaultComprobanteElectronico(DefaultComprobanteElectronico defaultComprobanteElectronico) {
        this.defaultComprobanteElectronico = defaultComprobanteElectronico;
    }

    public DefaultComprobanteElectronico getDefaultComprobanteElectronico() {
        return defaultComprobanteElectronico;
    }
    
    public void setStringSigned(String stringSigned) {
        this.stringSigned = stringSigned;
    }
    
    public String getStringSigned() {
        return stringSigned;
    }

    public void setPdfByte(byte[] pdfByte) {
        this.pdfByte = pdfByte;
    }

    public byte[] getPdfByte() {
        return pdfByte;
    }

    public void setFileCabecera(File fileCabecera) {
        this.fileCabecera = fileCabecera;
    }

    public File getFileCabecera() {
        return fileCabecera;
    }

    public void setFileDetalle(File fileDetalle) {
        this.fileDetalle = fileDetalle;
    }

    public File getFileDetalle() {
        return fileDetalle;
    }

    public void setCabeceraArray(String[][] cabeceraArray) {
        this.cabeceraArray = cabeceraArray;
    }

    public String[][] getCabeceraArray() {
        return cabeceraArray;
    }

    public void setDetalleArray(String[][] detalleArray) {
        this.detalleArray = detalleArray;
    }

    public String[][] getDetalleArray() {
        return detalleArray;
    }
    
    public void setRespuestaComprobante(RespuestaComprobante respuestaComprobante) {
        this.respuestaComprobante = respuestaComprobante;
    }

    public RespuestaComprobante getRespuestaComprobante() {
        return respuestaComprobante;
    }

    public void setRespuestaSolicitud(RespuestaSolicitud respuestaSolicitud) {
        this.respuestaSolicitud = respuestaSolicitud;
    }

    public RespuestaSolicitud getRespuestaSolicitud() {
        return respuestaSolicitud;
    }

    public void setDocumentW3C(Document documentW3C) {
        this.documentW3C = documentW3C;
    }

    public Document getDocumentW3C() {
        return documentW3C;
    }

    public void setDbDocumentFilter(String[][] dbDocumentFilter) {
        this.dbDocumentFilter = dbDocumentFilter;
    }

    public String[][] getDbDocumentFilter() {
        return dbDocumentFilter;
    }

    public void setEstadoDocumento(String estadoDocumento) throws Exception{
        if(estadoDocumento == null)
        {
            throw new Exception("El estado asignado no puede ser null");
        }
        else if(estadoDocumento.trim().equals(""))
        {
            throw new Exception("El estado asignado no puede vacio");
        }
        else if(
                estadoDocumento.trim().equals(DOCUMENTO_DEVUELTA) || 
                estadoDocumento.trim().equals(DOCUMENTO_RECIBIDA) || 
                estadoDocumento.trim().equals(DOCUMENTO_NO_AUTORIZADO) ||
                estadoDocumento.trim().equals(DOCUMENTO_AUTORIZADO)
                )
        {
            this.estadoDocumento = estadoDocumento;
        }
        else
        {
            throw new Exception("El estado asignado no es valido, encontrado: \"" + estadoDocumento + "\", esperado: " + 
                    DOCUMENTO_DEVUELTA + ", "+
                    DOCUMENTO_RECIBIDA + ", "+
                    DOCUMENTO_NO_AUTORIZADO + ", "+
                    DOCUMENTO_AUTORIZADO);
        }
    }

    public String getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setTipoIntegracion(String tipoIntegracion) {
        this.tipoIntegracion = tipoIntegracion;
    }

    public String getTipoIntegracion() {
        return tipoIntegracion;
    }
}
