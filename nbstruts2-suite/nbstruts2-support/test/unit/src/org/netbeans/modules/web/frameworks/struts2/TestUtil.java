package org.netbeans.modules.web.frameworks.struts2;

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import javax.swing.text.Document;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.xml.text.syntax.XMLKit;
import org.netbeans.modules.xml.xam.ModelSource;
import org.netbeans.modules.xml.xam.dom.AbstractDocumentModel;
import org.netbeans.modules.xml.xam.dom.DocumentModel;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.filesystems.URLMapper;

/**
 *
 * @author nn136682
 */
public class TestUtil {
    static {
        //JEditorPane.registerEditorKitForContentType(SchemaDataLoader.MIME_TYPE, XMLKit.class.getName());
        registerXMLKit();
    }
    
    public static Document loadResourceDocument(String path) throws Exception {
        URL url = TestUtil.class.getResource(path);
        FileObject fo = URLMapper.findFileObject(url);
        Document doc = getResourceAsDocument(path);
        doc.putProperty(Document.StreamDescriptionProperty, fo);
        
        return doc;
    }
    
    public static void registerXMLKit() {
        String[] path = new String[] { "Editors", "text", "x-xml" };
        FileObject target = Repository.getDefault().getDefaultFileSystem().getRoot();
        try {
            for (int i=0; i<path.length; i++) {
                FileObject f = target.getFileObject(path[i]);
                if (f == null) {
                    f = target.createFolder(path[i]);
                }
                target = f;
            }
            String name = "EditorKit.instance";
            if (target.getFileObject(name) == null) {
                FileObject f = target.createData(name);
                f.setAttribute("instanceClass", "org.netbeans.modules.xml.text.syntax.XMLKit");
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Document getResourceAsDocument(String path) throws Exception {
        InputStream in = TestUtil.class.getResourceAsStream(path);
        return loadDocument(in);
    }

    public static Document loadDocument(InputStream in) throws Exception {
        Document sd = new org.netbeans.editor.BaseDocument(
                org.netbeans.modules.xml.text.syntax.XMLKit.class, false);
        return setDocumentContentTo(sd, in);
    }
    
    public static Document setDocumentContentTo(Document doc, InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuffer sbuf = new StringBuffer();
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                sbuf.append(line);
                sbuf.append(System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        doc.remove(0, doc.getLength());
        doc.insertString(0,sbuf.toString(),null);
        return doc;
    }
    
    public static Document setDocumentContentTo(Document doc, String resourcePath) throws Exception {
        return setDocumentContentTo(doc, TestUtil.class.getResourceAsStream(resourcePath));
    }

    public static void setDocumentContentTo(DocumentModel model, String resourcePath) throws Exception {
        setDocumentContentTo(((AbstractDocumentModel)model).getBaseDocument(), resourcePath);
        model.sync();
    }
    
    public static int count = 0;
    public static StrutsModel loadStrutsModel(String resourcePath) throws Exception {
        URI locationURI = new URI(resourcePath);
        TestCatalogModel.getDefault().addURI(locationURI, getResourceURI(resourcePath));
        ModelSource ms = TestCatalogModel.getDefault().getModelSource(locationURI); 
        return StrutsModelFactory.getInstance().getModel(ms);
    }
    
    public static StrutsModel loadStrutsModel(File schemaFile) throws Exception {
        URI locationURI = new URI(schemaFile.getName());
        TestCatalogModel.getDefault().addURI(locationURI, schemaFile.toURI());
        ModelSource ms = TestCatalogModel.getDefault().getModelSource(locationURI);
        return StrutsModelFactory.getInstance().getModel(ms);
    }
    
    public static void dumpToStream(Document doc, OutputStream out) throws Exception{
        PrintWriter w = new PrintWriter(out);
        w.print(doc.getText(0, doc.getLength()));
        w.close();
        out.close();
    }
    
    public static void dumpToFile(DocumentModel model, File f) throws Exception {
        dumpToFile(((AbstractDocumentModel)model).getBaseDocument(), f);
    }
    
    public static void dumpToFile(Document doc, File f) throws Exception {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        PrintWriter w = new PrintWriter(out);
        w.print(doc.getText(0, doc.getLength()));
        w.close();
        out.close();
    }
    
    public static StrutsModel dumpAndReloadModel(StrutsModel sm) throws Exception {
        return dumpAndReloadModel(sm.getModelSource().getLookup().lookup(Document.class));
    }
    
    public static File dumpToTempFile(Document doc) throws Exception {
        File f = File.createTempFile("faces-config-tmp", "xml");
        System.out.println("file: " + f.getAbsolutePath());
        dumpToFile(doc, f);
        return f;
    }
    
    public static StrutsModel dumpAndReloadModel(Document doc) throws Exception {
        File f = dumpToTempFile(doc);
        URI dumpURI = new URI("dummyDump" + count++);
        TestCatalogModel.getDefault().addURI(dumpURI, f.toURI());
        ModelSource ms = TestCatalogModel.getDefault().getModelSource(dumpURI);
        return StrutsModelFactory.getInstance().getModel(ms);
    }
    
    public static Document loadDocument(File f) throws Exception {
        InputStream in = new BufferedInputStream(new FileInputStream(f));
        return loadDocument(in);
    }
        
    public static URI getResourceURI(String path) throws RuntimeException {
        try {
            return TestUtil.class.getResource(path).toURI();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static File getTempDir(String path) throws Exception {
        File tempdir = new File(System.getProperty("java.io.tmpdir"), path);
        tempdir.mkdirs();
        return tempdir;
    }
    
    public static String createXMLConfigText(String snippet) {
        return "<?xml version='1.0' encoding='UTF-8'?>" +
                "<!DOCTYPE struts PUBLIC" +
                "   \"-//Apache Software Foundation//DTD Struts Configuration 2.0//EN\"" +
                "   \"http://struts.apache.org/dtds/struts-2.0.dtd\">" +
                "   <struts>" +
                snippet +
                "</struts>";
    }
    
    public static BaseDocument createStrutsXMLConfigDocument(String content) throws Exception {
        //Class<?> kitClass = CloneableEditorSupport.getEditorKit(Struts2DataLoader.REQUIRED_MIME).getClass();
        BaseDocument doc = new BaseDocument(false, "text/xml");
        doc.insertString(0, content, null);
        return doc;
    }
}
