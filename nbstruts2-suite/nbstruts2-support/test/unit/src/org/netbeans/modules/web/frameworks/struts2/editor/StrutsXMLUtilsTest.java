package org.netbeans.modules.web.frameworks.struts2.editor;

import java.util.List;
import javax.swing.text.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.editor.BaseDocument;
import static org.junit.Assert.*;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import org.netbeans.modules.xml.text.syntax.SyntaxElement;
import org.netbeans.modules.xml.text.syntax.XMLSyntaxSupport;
import org.netbeans.modules.xml.text.syntax.dom.EmptyTag;
import org.netbeans.modules.xml.text.syntax.dom.StartTag;
import org.netbeans.modules.xml.text.syntax.dom.Tag;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Node;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public class StrutsXMLUtilsTest {

    public StrutsXMLUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetPackageTag() throws Exception {
        String config = TestUtil.createXMLConfigText("<package name='mailreader-default' " +
                "    namespace='/' extends='struts-default'>" +
                "    <interceptors>" +
                "        <interceptor name='authentication' class='mailreader2.AuthenticationInterceptor'/>" +
                "        <interceptor-stack name='user' >" +
                "            <interceptor-ref name='authentication'/>" +
                "        </interceptor-stack>" +
                "    </interceptors>" + 
                "</package>");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(config);
        XMLSyntaxSupport syntaxSupport = new XMLSyntaxSupport(doc);
        SyntaxElement syntaxElement = syntaxSupport.getElementChain(config.indexOf("interceptor name"));
        assertTrue(syntaxElement instanceof EmptyTag);
        Tag tag = (Tag) syntaxElement;
        Node packageNode = StrutsXMLUtils.getPackageTag(tag);
        Tag packageTag = (Tag) packageNode;
        assertEquals(config.indexOf("<package"), packageTag.getElementOffset());
        
        syntaxElement = syntaxSupport.getElementChain(config.indexOf("package name"));
        assertTrue(syntaxElement instanceof StartTag);
        assertEquals(config.indexOf("<package"), ((Tag)StrutsXMLUtils.getPackageTag((Tag)syntaxElement)).getElementOffset());
        
        config = TestUtil.createXMLConfigText("");
        doc = TestUtil.createStrutsXMLConfigDocument(config);
        tag = (Tag) (new XMLSyntaxSupport(doc)).getElementChain(config.indexOf("struts>"));
        assertNull(StrutsXMLUtils.getPackageTag(tag));
    }
    
    @Test
    public void testHasAttribute() throws Exception {
        String config = TestUtil.createXMLConfigText("<package name='mailreader-default' " +
                "    namespace='/' extends='struts-default'>" +
                "    <interceptors>" +
                "        <interceptor name='authentication' class='mailreader2.AuthenticationInterceptor'/>" +
                "        <interceptor-stack name='user' >" +
                "            <interceptor-ref name='authentication'/>" +
                "        </interceptor-stack>" +
                "    </interceptors>" +
                "</package>");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(config);
        XMLSyntaxSupport syntaxSupport = new XMLSyntaxSupport(doc);
        SyntaxElement syntaxElement = syntaxSupport.getElementChain(config.indexOf("interceptor name"));
        Tag tag = (Tag) syntaxElement;
        assertTrue(StrutsXMLUtils.hasAttribute(tag, "name"));
        assertFalse(StrutsXMLUtils.hasAttribute(tag, "hhhh"));
    }
    
    @Test
    public void testGetElementsByName() throws Exception {
        String config = TestUtil.createXMLConfigText("<package name='mailreader-default' " +
                "    namespace='/' extends='struts-default'>" +
                "    <interceptors>" +
                "        <interceptor name='authentication' class='mailreader2.AuthenticationInterceptor'/>" +
                "        <interceptor-stack name='user' >" +
                "            <interceptor-ref name='authentication'/>" +
                "        </interceptor-stack>" +
                "    </interceptors>" +
                "</package>");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(config);
        XMLSyntaxSupport syntaxSupport = new XMLSyntaxSupport(doc);
        SyntaxElement syntaxElement = syntaxSupport.getElementChain(config.indexOf("interceptors>"));
        Tag tag = (Tag) syntaxElement;
        List<Node> nodes = StrutsXMLUtils.getElementsByName(tag, "interceptor");
        assertEquals(1, nodes.size());
        
        nodes = StrutsXMLUtils.getElementsByName(tag, "foobar");
        assertEquals(0, nodes.size());
    }
    
    @Test
    public void testVisibleIncludes() throws Exception {
        Document doc = TestUtil.loadResourceDocument("api/configmodel/resources/struts-mailreader.xml");
        List<FileObject> visibleIncFiles = StrutsXMLUtils.getVisibleIncludes(doc, 431);
        assertEquals(1, visibleIncFiles.size());
        assertEquals("mailreader-default.xml", visibleIncFiles.get(0).getNameExt());
        
        visibleIncFiles = StrutsXMLUtils.getVisibleIncludes(doc, 476);
        assertEquals(2, visibleIncFiles.size());
        assertEquals("mailreader-default.xml", visibleIncFiles.get(0).getNameExt());
        assertEquals("mailreader-support.xml", visibleIncFiles.get(1).getNameExt());
    }
    
    @Test
    public void testVisiblePackages() throws Exception {
        String config = TestUtil.createXMLConfigText("<package name='mailreader-default' " +
                "namespace='/' extends='struts-default'/>" + 
                "<package name='mailreader-2' " +
                "namespace='/' extends='struts-default'/>");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(config);
        List<Node> visiblePackages = StrutsXMLUtils.getVisiblePackages(doc, 
                config.indexOf("<package name='mailreader-2'"));
        assertEquals(1, visiblePackages.size());
        visiblePackages = StrutsXMLUtils.getVisiblePackages(doc, config.indexOf("</struts>"));
        assertEquals(2, visiblePackages.size());
    }
}