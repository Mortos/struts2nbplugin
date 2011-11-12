package org.netbeans.modules.web.frameworks.struts2.utils;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.editor.StrutsXMLUtils;
import static org.junit.Assert.*;
import org.w3c.dom.Node;

/**
 *
 * @author Rohan Ranade
 */
public class PackageHierarchyTraverserTest {

    public PackageHierarchyTraverserTest() {
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
    public void localTraversalTest() throws Exception {
        final List<String> traversedList = new ArrayList<String>();
        
        String config = TestUtil.createXMLConfigText(
                "<package name='first' namespace='/'>" +
                "</package>" +
                "<package name='second' namespace='/' extends='first'>" +
                "</package>" +
                "<package name='third' namespace='/' extends='second'>" +
                "    <interceptors>" +
                "    </interceptors>" + 
                "</package>"
                );
        BaseDocument document = TestUtil.createStrutsXMLConfigDocument(config);
        PackageHierarchyTraverser pht = new PackageHierarchyTraverser(document, config.indexOf("<interceptors>"), false);
        pht.traverse(new PackageProcessor() {
            public void process(Node node) {
                traversedList.add(StrutsXMLUtils.getAttribute(node, StrutsAttributes.NAME.getName()));
            }
        });
        
        assertEquals("third", traversedList.get(0));
        assertEquals("second", traversedList.get(1));
        assertEquals("first", traversedList.get(2));
    }
    
    @Test
    public void circularDepTraversalTest() throws Exception {
        String config = TestUtil.createXMLConfigText(
                "<package name='first' namespace='/' extends='second'>" +
                "</package>" +
                "<package name='second' namespace='/' extends='first'>" +
                "</package>" +
                "<package name='third' namespace='/' extends='second'>" +
                "    <interceptors>" +
                "    </interceptors>" + 
                "</package>"
                );
        // test that there is no infinite loop
        BaseDocument document = TestUtil.createStrutsXMLConfigDocument(config);
        PackageHierarchyTraverser pht = new PackageHierarchyTraverser(document, config.indexOf("<interceptors>"));
        pht.traverse(new PackageProcessor() {
            public void process(Node node) {
                // do nothing
            }
        });
        
        assertTrue("Test passed as no infinite loop", true);
    }
}