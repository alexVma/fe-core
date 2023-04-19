/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core.portal.webService;

import java.io.File;
import java.io.FileInputStream;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.WebServiceContext;
import org.inprofsolutions.sri.fe.core.portal.webService.soapObject.DefaultBaseComprobante;
import org.inprofsolutions.sri.fe.core.portal.webService.soapObject.ListaComprobantes;
import org.inprofsolutions.utils.dataBase.DataBaseConnectionWizard;

/**
 *
 * @author caf
 */
@WebService() //this binds the SEI to the SIB
public class PortalImpl implements WebServiceInterface 
{
    DataBaseConnectionWizard db = null;
    
    @Resource
    WebServiceContext webServiceContext;

    public PortalImpl(DataBaseConnectionWizard db) 
    {
        this.db = db;
    }
    
    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="ListaComprobantes")
    public ListaComprobantes getDocumentos(@XmlElement(required=true)@WebParam(name = "usuario") String usuario,@XmlElement(required=true)@WebParam(name = "clave") String clave) throws Exception
    {    
//        if(!checkAuthentication())
//        {
//            LoggerContainer.getListLogger().get(0).log(Level.INFO,"Authentication error");
//            throw new Exception("Authentication error");
//        }
        
        String loginSQL = "select trim(taxid) as ci_ruc,\n" +
            "trim(em_atimdm_email) as email\n" +
            "from c_bpartner\n" +
            "where taxid='"+usuario.trim()+"'\n" +
            "and ad_client_id='1F7598333F69476DB30ABDFB61892E41'";
        
        String loginResultado [][] = db.sqlArray(loginSQL);
        
        if(loginResultado == null)
        {
            //return new ListaComprobantes(false);
        }
        else if(loginResultado.length == 0)
        {
            //return new ListaComprobantes(false);
        }
        else if(loginResultado[0].length == 2)
        {
            if(loginResultado[0][1] != null)
            {
                if(loginResultado[0][1].equals(clave.trim()))
                {
                    ListaComprobantes listaComprobantes = new ListaComprobantes(true);

                    String resultadoComprobantes [][] = db.sqlArray("SELECT \n" +
                    "cod_local, pto_emision, documentno, fecha, clave_acceso, num_autorizacion\n" +
                    "FROM indfe_resultado\n" +
                    "where \n" +
                    "ruc_ci like '"+usuario.trim()+"'\n" +
                    "and\n" +
                    "resultado like 'AUTORIZADO'"); 

                    if(resultadoComprobantes != null)
                    {
                        for(int i = 0 ; i < resultadoComprobantes.length ; i++)
                        {
                            listaComprobantes.getComprobantes().getComprobantes().add(new DefaultBaseComprobante(resultadoComprobantes[i][0], resultadoComprobantes[i][1], resultadoComprobantes[i][2], resultadoComprobantes[i][3], resultadoComprobantes[i][4], resultadoComprobantes[i][5]));
                        }
                    }
                    return listaComprobantes;
                }
                else
                {
                    System.out.println("Usuario: \""+usuario.trim()+"\", Clave Ingresada \""+clave.trim()+"\" Clave del usuario: \""+loginResultado[0][1]+"\" No coincide");
                }
            }   
        }
        else
        {
            System.out.println("Usuario: \""+usuario.trim()+"\"No encontrado");
        }
        return new ListaComprobantes(false);
    }
    
    @WebMethod//(operationName = "getResultados1")
    @WebResult(name="DocumentoFile")
    public byte [] getDocumentoFile(@XmlElement(required=true)@WebParam(name = "usuario") String usuario,@XmlElement(required=true)@WebParam(name = "clave") String clave,@XmlElement(required=true)@WebParam(name = "establecimiento") String establecimiento,@XmlElement(required=true)@WebParam(name = "puntoEmision") String puntoEmision,@XmlElement(required=true)@WebParam(name = "secuencial") String secuencial,@XmlElement(required=true)@WebParam(name = "tipo") String tipo) throws Exception
    {
        String loginResultado [][] = db.sqlArray("select trim(taxid) as ci_ruc,\n" +
        "trim(em_atimdm_email) as email\n" +
        "from c_bpartner\n" +
        "where taxid='"+usuario+"'\n" +
        "and ad_client_id='1F7598333F69476DB30ABDFB61892E41'");
        
        if(loginResultado == null)
        {
            return null;
        }
        else if(loginResultado.length == 0)
        {
            return null;
        }
        else if(loginResultado[0].length == 2)
        {
            if(loginResultado[0][1] != null)
            {
                if(loginResultado[0][1].equals(clave.trim()))
                {
                    String sqlConsulta = "SELECT \n" +
                    "count(*)\n" +
                    "FROM indfe_resultado\n" +
                    "where \n" +
                    "ruc_ci like '"+usuario.trim()+"'\n" +
                    "and \n" +
                    "cod_local like '"+establecimiento+"'\n" +
                    "and\n" +
                    "pto_emision like '"+puntoEmision+"'\n" +
                    "and\n" +
                    "documentno like '"+secuencial+"'\n" +
                    "and\n" +
                    "resultado like 'AUTORIZADO'";

                    String validacionComprobante [][] = db.sqlArray(sqlConsulta); 

                    if(validacionComprobante != null)
                    {
                        if(validacionComprobante.length > 0)
                        {
                            if(validacionComprobante[0][0].equals("1"))
                            {
                                if(tipo != null)
                                {
                                    if(tipo.equals("pdf") || tipo.equals("xml"))
                                    {
                                        //FileInputStream fileInputStream = new FileInputStream("/home/caf/Documentos/NetBeansProjects/SRI/workspace/FE/INDUMOT_DOCUMENTOS/autorizados/FACTURA_"+establecimiento+"-"+puntoEmision+"-"+secuencial+"."+tipo);
                                        FileInputStream fileInputStream = new FileInputStream("Documentos"+File.separator+"autorizados"+File.separator+"FACTURA_"+establecimiento+"-"+puntoEmision+"-"+secuencial+"."+tipo);
                                        byte archivo [] = new byte[fileInputStream.available()];
                                        fileInputStream.read(archivo);
                                        return archivo;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
 }
