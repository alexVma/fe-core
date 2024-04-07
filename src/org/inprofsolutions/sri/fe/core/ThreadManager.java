/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inprofsolutions.sri.fe.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.inprofsolutions.sri.fe.core.integrator.webService.ProcesarDocumentoImpl;
import org.inprofsolutions.sri.fe.core.portal.webService.PortalImpl;
import org.inprofsolutions.sri.fe.core.taskProcessor.ColectorCSVProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.MailProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.WebServiceRequesterProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.WebServiceResponderProcessor;
import org.inprofsolutions.utils.properties.Propiedades;
import org.inprofsolutions.utils.queueProcessor.QueueProcessorSingleArrayList;
import org.inprofsolutions.utils.webService.WebServicePublisher;
import org.inprofsolutions.utils.logging.LoggerContainer;
import org.inprofsolutions.sri.fe.core.taskProcessor.CheckAmbienteProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.SriAutorizacionProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.ColectorDBProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.DBUpdaterProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.SriRecepcionProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.XMLW3CGeneratorProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.ColectorXMLProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.XMLObjectGeneratorProcessor;
import org.inprofsolutions.sri.fe.core.taskProcessor.XMLSignerProcessor;
import org.inprofsolutions.utils.appArguments.developer;
import org.inprofsolutions.utils.cryptography.CifradorAESCBCPKCS5Padding;
import org.inprofsolutions.utils.dataBase.DataBaseConnectionWizard;

/**
 *
 * @author caf
 */
public class ThreadManager 
{
    private CifradorAESCBCPKCS5Padding cifrador = null;
    
    public static void main(String[] args) {
        new ThreadManager(args,"Tipo2");
    }
    
    public ThreadManager(String AppArgumentos [],String tipoEndPage)
    {
        //DeveloperDirectory /home/caf/NetBeansProjects/SRI/workspace/app/
        
        if(AppArgumentos != null)
        {
            for(int i = 0; i < AppArgumentos.length;i++)
            {
                if(AppArgumentos[i]!= null)
                {
                    if(!AppArgumentos[i].trim().equals(""))
                    {
                        switch (AppArgumentos[i].trim()) 
                        {
                            case "--debug":
                                System.out.println("Argumento configurado: " + AppArgumentos[i].trim());
                                developer.setDebug(true);
                                break;
                            default: 
                                System.out.println("Argumento no soportado: " + AppArgumentos[i].trim());
                                break;
                        }
                    }
                }
            }
        }
        
        loadAESCipher();
        
        Propiedades pro = new Propiedades();
        
        File directorioActual = new File(".");
        FilenameFilter filter = new FilenameFilter() 
        {
            public boolean accept(File directory, String fileName) 
            {
                return fileName.endsWith(".conf");
            }
        };
        
        String resConf [] = directorioActual.list(filter);
        if(resConf.length>0)
        {
            try
            {
                pro.load(resConf[0]);
                
                loadLogger("log-"+pro.leer("RUC")+"_%g.log",pro.leer("PathDirectorio")+File.separator+"log",developer.isDebug());
                
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"java.io.File.separator=\""+java.io.File.separator+"\"");
                LoggerContainer.getListLogger().get(0).log(Level.INFO,"java.io.File.pathSeparator=\""+java.io.File.pathSeparator+"\"");
                
                WebServiceRequesterProcessor webServiceRequesterProcessor = null;
                WebServiceResponderProcessor webServiceResponderProcessor = null;
                ColectorCSVProcessor colectorCSVProcessor = null;
                
                XMLW3CGeneratorProcessor xMLGeneratorProcessor = null;
                XMLSignerProcessor xMLSignerProcessor = null;
                
                SriRecepcionProcessor recepcionSriProcessor = null;
                SriAutorizacionProcessor autorizacionSriProcessor = null;
                
                MailProcessor mailProcessor = null;
                
                String tipoIntegracion = pro.leer("TipoIntegacion");
                if(tipoIntegracion == null)
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"No se ha seleccionado el tipo de integracion, no se iniciara el proceso: null");
                }
                else if(tipoIntegracion.equals(""))
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"No se ha seleccionado el tipo de integracion, no se iniciara el proceso: String vacio");
                }
                else if(tipoIntegracion.equals("WS"))
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"Tipo de integracion seleccionada: WS, Iniciando el Servicio");
                    System.out.println("Tipo de integracion seleccionada: WS, Iniciando el Servicio");
                    webServiceRequesterProcessor = new WebServiceRequesterProcessor(10, 10, 60, 10000);
                    CheckAmbienteProcessor checkAmbienteProcessor = new CheckAmbienteProcessor(10, 10, 60, 10000, pro);
                    xMLGeneratorProcessor = new XMLW3CGeneratorProcessor(10, 10, 60, 10000,pro);
                    System.out.println("cifrador.desencriptar(pro.leer(\"ClaveFirma\") "+cifrador.desencriptar(pro.leer("ClaveFirma")));
                    xMLSignerProcessor = new XMLSignerProcessor(10, 10, 60, 10000,pro,cifrador.desencriptar(pro.leer("ClaveFirma")));
                    recepcionSriProcessor = new SriRecepcionProcessor(10, 10, 60, 10000, pro);
                    autorizacionSriProcessor = new SriAutorizacionProcessor(10, 10, 60, 10000,recepcionSriProcessor, pro,tipoEndPage);
                    mailProcessor = new MailProcessor(10, 10, 60, 10000, pro, cifrador);
                    webServiceResponderProcessor = new WebServiceResponderProcessor(10, 10, 60, 10000);                    
                    
                    webServiceRequesterProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    checkAmbienteProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    xMLGeneratorProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    xMLSignerProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    recepcionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    autorizacionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    mailProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    webServiceResponderProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    
                    webServiceRequesterProcessor.getOutData().getData().add(checkAmbienteProcessor.getInData().getData().get(0));
                    checkAmbienteProcessor.getOutData().getData().add(xMLGeneratorProcessor.getInData().getData().get(0));
                    xMLGeneratorProcessor.getOutData().getData().add(xMLSignerProcessor.getInData().getData().get(0));
                    xMLSignerProcessor.getOutData().getData().add(recepcionSriProcessor.getInData().getData().get(0));
                    recepcionSriProcessor.getOutData().getData().add(autorizacionSriProcessor.getInData().getData().get(0));
                    autorizacionSriProcessor.getOutData().getData().add(webServiceResponderProcessor.getInData().getData().get(0)); //notificar al web service o BaseDeDatos
                    autorizacionSriProcessor.getOutData().getData().add(mailProcessor.getInData().getData().get(0)); //notificar al mail
                    
                    webServiceResponderProcessor.start();
                    webServiceRequesterProcessor.start();
                    checkAmbienteProcessor.start();
                    xMLGeneratorProcessor.start();
                    xMLSignerProcessor.start();
                    recepcionSriProcessor.start();
                    autorizacionSriProcessor.start();
                    mailProcessor.start();
                    
//                    WebServicePublisher webServicePublisher = new WebServicePublisher("security"+File.separator+"ws-ssl-store.jks","test56","test56","0.0.0.0",9000,1000,100);
                    WebServicePublisher webServicePublisher = new WebServicePublisher("127.0.0.1",9000,0,20);
               
                    webServicePublisher.publishWS("/WsFacturaLista", new ProcesarDocumentoImpl(webServiceRequesterProcessor, webServiceResponderProcessor));
                }
                else if(tipoIntegracion.equals("CSV"))
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"Tipo de integracion seleccionada: CSV, Iniciando el Servicio");
                    
                    colectorCSVProcessor  = new ColectorCSVProcessor(10, 10, 60, 10000, pro);
                    XMLObjectGeneratorProcessor xMLObjectGeneratorProcessor = new XMLObjectGeneratorProcessor(10, 10, 60, 10000,pro);
                    CheckAmbienteProcessor checkAmbienteProcessor = new CheckAmbienteProcessor(10, 10, 60, 10000, pro);
                    xMLGeneratorProcessor = new XMLW3CGeneratorProcessor(10, 10, 60, 10000,pro);
                    xMLSignerProcessor = new XMLSignerProcessor(10, 10, 60, 10000,pro,cifrador.desencriptar(pro.leer("ClaveFirma")));
                    recepcionSriProcessor = new SriRecepcionProcessor(10, 10, 60, 10000, pro);
                    autorizacionSriProcessor = new SriAutorizacionProcessor(10, 10, 60, 10000,recepcionSriProcessor, pro,tipoEndPage);
                    mailProcessor = new MailProcessor(10, 10, 60, 10000, pro, cifrador);
                    
                    xMLObjectGeneratorProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    checkAmbienteProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    xMLGeneratorProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    xMLSignerProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    recepcionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    autorizacionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    mailProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    
                    colectorCSVProcessor.getOutData().getData().add(xMLObjectGeneratorProcessor.getInData().getData().get(0));
                    xMLObjectGeneratorProcessor.getOutData().getData().add(checkAmbienteProcessor.getInData().getData().get(0));
                    checkAmbienteProcessor.getOutData().getData().add(xMLGeneratorProcessor.getInData().getData().get(0));
                    xMLGeneratorProcessor.getOutData().getData().add(xMLSignerProcessor.getInData().getData().get(0));
                    xMLSignerProcessor.getOutData().getData().add(recepcionSriProcessor.getInData().getData().get(0));
                    recepcionSriProcessor.getOutData().getData().add(autorizacionSriProcessor.getInData().getData().get(0));
                    autorizacionSriProcessor.getOutData().getData().add(null); //notificar al web service o BaseDeDatos
                    autorizacionSriProcessor.getOutData().getData().add(mailProcessor.getInData().getData().get(0)); //notificar al mail
                    
                    colectorCSVProcessor.start();
                    xMLObjectGeneratorProcessor.start();
                    checkAmbienteProcessor.start();
                    xMLGeneratorProcessor.start();
                    xMLSignerProcessor.start();
                    recepcionSriProcessor.start();
                    autorizacionSriProcessor.start();
                    mailProcessor.start();
                }
                else if(tipoIntegracion.equals("DBSQL"))
                {
//                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"Tipo de integracion seleccionada: DBSQL, No soportada en esta version");
                    
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"Tipo de integracion seleccionada: DBSQL, Iniciando el Servicio");
                    
                    DataBaseConnectionWizard db = null;
                    try
                    {
                        db = new DataBaseConnectionWizard("org.postgresql.Driver");
                        db.conectar("jdbc:postgresql://IP/DB", "USER", "PASSWORD");
                    }
                    catch(Exception e)
                    {
                        LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
                    }
                    
                    try
                    {
                        WebServicePublisher webServicePublisherPortal = new WebServicePublisher("security"+File.separator+"ws-ssl-store.jks","test56","test56","0.0.0.0",8080,1000,100);
                        webServicePublisherPortal.publishWS("/WsPortal", new PortalImpl(db));
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error al cargar el portal");
                        e.printStackTrace();
                    }
                    
                    ColectorDBProcessor colectorDBProcessor = new ColectorDBProcessor(10, 10, 60, 10000,pro,db);
                    CheckAmbienteProcessor checkAmbienteProcessor = new CheckAmbienteProcessor(10, 10, 60, 10000, pro);
                    xMLGeneratorProcessor = new XMLW3CGeneratorProcessor(10, 10, 60, 10000,pro);
                    xMLSignerProcessor = new XMLSignerProcessor(10, 10, 60, 10000,pro,cifrador.desencriptar(pro.leer("ClaveFirma")));
                    recepcionSriProcessor = new SriRecepcionProcessor(10, 10, 60, 10000, pro);
                    autorizacionSriProcessor = new SriAutorizacionProcessor(10, 10, 60, 10000,recepcionSriProcessor, pro,tipoEndPage);
                    mailProcessor = new MailProcessor(10, 10, 60, 10000, pro, cifrador);
                    DBUpdaterProcessor dBUpdaterProcessor = new DBUpdaterProcessor(10, 10, 60, 10000,db);
                    
                    colectorDBProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    checkAmbienteProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    xMLGeneratorProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    xMLSignerProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    recepcionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    autorizacionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    mailProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    dBUpdaterProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    
                    colectorDBProcessor.getOutData().getData().add(checkAmbienteProcessor.getInData().getData().get(0));
                    checkAmbienteProcessor.getOutData().getData().add(xMLGeneratorProcessor.getInData().getData().get(0));
                    xMLGeneratorProcessor.getOutData().getData().add(xMLSignerProcessor.getInData().getData().get(0));
                    xMLSignerProcessor.getOutData().getData().add(recepcionSriProcessor.getInData().getData().get(0));
                    recepcionSriProcessor.getOutData().getData().add(autorizacionSriProcessor.getInData().getData().get(0));
                    recepcionSriProcessor.getOutData().getData().add(dBUpdaterProcessor.getInData().getData().get(0)); //notificar a la BaseDeDatos
                    autorizacionSriProcessor.getOutData().getData().add(dBUpdaterProcessor.getInData().getData().get(0)); //notificar al web service o BaseDeDatos
                    autorizacionSriProcessor.getOutData().getData().add(mailProcessor.getInData().getData().get(0)); //notificar al mail
                    
                    colectorDBProcessor.start();
                    checkAmbienteProcessor.start();
                    xMLGeneratorProcessor.start();
                    xMLSignerProcessor.start();
                    recepcionSriProcessor.start();
                    autorizacionSriProcessor.start();
                    mailProcessor.start();
                    dBUpdaterProcessor.start();
                }
                else if(tipoIntegracion.equals("XMLSF"))
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"Tipo de integracion seleccionada: XMLSF, Iniciando el Servicio");
                    
                    ColectorXMLProcessor colectorXMLProcessor = new ColectorXMLProcessor(10, 10, 60, 10000, pro);
                    xMLSignerProcessor = new XMLSignerProcessor(10, 10, 60, 10000,pro,cifrador.desencriptar(pro.leer("ClaveFirma")));
                    recepcionSriProcessor = new SriRecepcionProcessor(10, 10, 60, 10000, pro);
                    autorizacionSriProcessor = new SriAutorizacionProcessor(10, 10, 60, 10000,recepcionSriProcessor, pro,tipoEndPage);
                    mailProcessor = new MailProcessor(10, 10, 60, 10000, pro, cifrador);
                    
                    xMLSignerProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    recepcionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    autorizacionSriProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    mailProcessor.getInData().getData().add(new QueueProcessorSingleArrayList());
                    
                    colectorXMLProcessor.getOutData().getData().add(xMLSignerProcessor.getInData().getData().get(0));
                    xMLSignerProcessor.getOutData().getData().add(recepcionSriProcessor.getInData().getData().get(0));
                    recepcionSriProcessor.getOutData().getData().add(autorizacionSriProcessor.getInData().getData().get(0));
                    autorizacionSriProcessor.getOutData().getData().add(null); //notificar al web service
                    autorizacionSriProcessor.getOutData().getData().add(mailProcessor.getInData().getData().get(0)); //notificar al mail
                    
                    colectorXMLProcessor.start();
                    xMLSignerProcessor.start();
                    recepcionSriProcessor.start();
                    autorizacionSriProcessor.start();
                    mailProcessor.start();
                }
                else 
                {
                    LoggerContainer.getListLogger().get(0).log(Level.INFO,"Tipo de integracion seleccionada: "+tipoIntegracion+", no se ha encontrado el tipo de integracion");
                }
            }
            catch(Exception e)
            {e.printStackTrace();
                LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
            }
        }
        else
        {
            System.out.println("No hay servicios configurados");
        }
    }
    
    public String [] listarConfig()
    {
        File directorioActual = new File(".");
        
        FilenameFilter filter = new FilenameFilter() 
        {
            public boolean accept(File directory, String fileName) 
            {
                return fileName.endsWith(".conf");
            }
        };
        
        String resConf [] = directorioActual.list(filter);
        return resConf;
    }
    
    public void saveConfig(Propiedades config, String nombre, String descripcion) throws IOException
    {
        config.save(nombre, descripcion);
    }
    
    public Propiedades openConfig(String archivo) throws IOException
    {
        Propiedades config = new Propiedades();
        config.load(archivo);
        return config;
    }
    
    public void elimiarConfig(String archivo)
    {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,archivo);
            new File(archivo).delete();
    }
    
    private void loadAESCipher()
    {
        try
        {
            byte[] CLAVE_BYTES = 
            {
                (byte)0x0E, (byte)0x87, (byte)0x45, (byte)0xE6, (byte)0x1A, (byte)0xCE, (byte)0xE4, (byte)0x72, (byte)0x4B, (byte)0xEE, (byte)0x4C, (byte)0x65, (byte)0x6B, (byte)0x2D, (byte)0x1E, (byte)0xCD
            };
            
            byte[] SALT_BYTES = 
            {
                (byte)0xA4, (byte)0xAF, (byte)0x31, (byte)0x09, (byte)0xE3, (byte)0x50, (byte)0xEF, (byte)0x9A, (byte)0x93, (byte)0x2B, (byte)0x1F, (byte)0x9F, (byte)0x11, (byte)0xCC, (byte)0x47, (byte)0x60, (byte)0x8D, (byte)0xA5, (byte)0xCD, (byte)0x5D
            };
            byte[] IV_BYTES = 
            {
                (byte)0x63, (byte)0xA1, (byte)0x11, (byte)0xF6, (byte)0xB1, (byte)0x1D, (byte)0xCF, (byte)0x4F, (byte)0x17, (byte)0x42, (byte)0x78, (byte)0x6D, (byte)0x27, (byte)0x54, (byte)0x26, (byte)0x5B
            };
            cifrador = new CifradorAESCBCPKCS5Padding(new String(CLAVE_BYTES),65536,128,IV_BYTES,SALT_BYTES);
        }
        catch(Exception e)
        {
            LoggerContainer.getListLogger().get(0).log(Level.INFO,e.toString());if(developer.isDebug()){e.printStackTrace();}
        }
    }
    
    private int loadLogger(String pattern, boolean appendConsole) throws IOException
    {
        return loadLogger(pattern, ".", appendConsole);
    }
    
    private int loadLogger(String pattern, String path, boolean appendConsole) throws IOException
    {
        Logger loggerCurrentCompany = Logger.getLogger("Logger" + pattern);
        
        //Deshabilita el uso del manejador padre, en efecto el Logger actual no reporta al padre, por lo cual no imprime en la consola System.err por defecto.
        loggerCurrentCompany.setUseParentHandlers(false);
        
        FileHandler fileHandler = new FileHandler(path+File.separator+pattern, 20000000, 100000, true);
        
        //Utiliza el formato por defecto
        //fileHandler.setFormatter(new SimpleFormatter());
        
        //Utiliza el formato formato definido en la siguiente estructura
        fileHandler.setFormatter(new SimpleFormatter()
        {
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd.hh:mm:ss.SSS");

            public String format(LogRecord record) {
                StringBuilder builder = new StringBuilder(1000);
                builder.append(df.format(new Date(record.getMillis()))).append(" - ");
                //builder.append("[").append(record.getSourceClassName()).append(".");
                //builder.append(record.getSourceMethodName()).append("] - ");
                builder.append("[").append(record.getLevel()).append("] - ");
                builder.append(formatMessage(record));
                builder.append("\r\n");
                return builder.toString();
            }
        });
        loggerCurrentCompany.addHandler(fileHandler);

        if(appendConsole)
        {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            
            //Utiliza el formato por defecto
            //consoleHandler.setFormatter(new SimpleFormatter());
            
            //Utiliza el formato formato definido en la siguiente estructura
            consoleHandler.setFormatter(new SimpleFormatter()
            {
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd.hh:mm:ss.SSS");

                public String format(LogRecord record) {
                    StringBuilder builder = new StringBuilder(1000);
                    builder.append(df.format(new Date(record.getMillis()))).append(" - ");
                    //builder.append("[").append(record.getSourceClassName()).append(".");
                    //builder.append(record.getSourceMethodName()).append("] - ");
                    builder.append("[").append(record.getLevel()).append("] - ");
                    builder.append(formatMessage(record));
                    builder.append("\r\n");
                    return builder.toString();
                }
            });
            loggerCurrentCompany.addHandler(consoleHandler);
        }
        
        LoggerContainer.getListLogger().add(loggerCurrentCompany);
        return LoggerContainer.getListLogger().size()-1;
    }
}