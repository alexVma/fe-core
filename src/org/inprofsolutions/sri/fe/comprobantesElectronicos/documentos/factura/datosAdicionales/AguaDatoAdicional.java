package org.inprofsolutions.sri.fe.comprobantesElectronicos.documentos.factura.datosAdicionales;

import java.util.List;

/**
 *
 * @author alexv
 */
public class AguaDatoAdicional {
    String lecturaAnterior;
    String lecturaActual;
    String consumo;
    String novedad;
    //////////////////////////////////////////////////////
    String tarifaCategoria;
    String nroMedidor;
    String emision;//auxiliar
    //////////////////////////////////////////////////////
    String nameSerie;
    List xSerieName;
    List ySerieValores;

    public AguaDatoAdicional() {
    }

    public String getLecturaAnterior() {
        return lecturaAnterior;
    }

    public void setLecturaAnterior(String lecturaAnterior) {
        this.lecturaAnterior = lecturaAnterior;
    }

    public String getLecturaActual() {
        return lecturaActual;
    }

    public void setLecturaActual(String lecturaActual) {
        this.lecturaActual = lecturaActual;
    }

    public String getConsumo() {
        return consumo;
    }

    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }

    public String getTarifaCategoria() {
        return tarifaCategoria;
    }

    public void setTarifaCategoria(String tarifaCategoria) {
        this.tarifaCategoria = tarifaCategoria;
    }

    public String getNroMedidor() {
        return nroMedidor;
    }

    public void setNroMedidor(String nroMedidor) {
        this.nroMedidor = nroMedidor;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public String getNameSerie() {
        return nameSerie;
    }

    public void setNameSerie(String nameSerie) {
        this.nameSerie = nameSerie;
    }

    public List getxSerieName() {
        return xSerieName;
    }

    public void setxSerieName(List xSerieName) {
        this.xSerieName = xSerieName;
    }

    public List getySerieValores() {
        return ySerieValores;
    }

    public void setySerieValores(List ySerieValores) {
        this.ySerieValores = ySerieValores;
    }

    public String getNovedad() {
        return novedad;
    }

    public void setNovedad(String novedad) {
        this.novedad = novedad;
    }
        
}
