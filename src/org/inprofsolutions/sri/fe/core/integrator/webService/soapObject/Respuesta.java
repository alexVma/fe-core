/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.integrator.webService.soapObject;

import sri.gob.ec.client.ws.recepcion.RespuestaSolicitud;
import sri.gob.ec.client.ws.autorizacion.RespuestaComprobante;

/**
 *
 * @author caf
 */
public class Respuesta 
{
    private RespuestaComprobante respuestaComprobante = null;
    private RespuestaSolicitud respuestaSolicitud = null;

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
}
