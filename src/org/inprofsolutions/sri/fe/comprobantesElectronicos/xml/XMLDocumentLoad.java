/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inprofsolutions.sri.fe.comprobantesElectronicos.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author caf
 */
public class XMLDocumentLoad 
{
    Document doc = null;
    DocumentBuilder db = null;

    public XMLDocumentLoad() throws ParserConfigurationException 
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        db = dbf.newDocumentBuilder();
    }
    
    public Document getGenerateDocument(File file) throws TransformerConfigurationException, TransformerException, SAXException, IOException
    {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte data [] = new byte[fileInputStream.available()];
        fileInputStream.read(data);
        fileInputStream.close();
        return getGenerateDocument(new String(data,"UTF-8"));
    }
    
    public Document getGenerateDocument(String sw) throws TransformerConfigurationException, TransformerException, SAXException, IOException
    {
        ByteArrayInputStream stream = new ByteArrayInputStream(sw.toString().getBytes("UTF-8"));
        return db.parse(stream);
    }
}