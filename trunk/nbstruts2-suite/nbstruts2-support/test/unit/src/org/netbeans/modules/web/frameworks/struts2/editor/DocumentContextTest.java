package org.netbeans.modules.web.frameworks.struts2.editor;

import javax.swing.text.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import static org.junit.Assert.*;

/**
 *
 * @author Rohan Ranade
 */
public class DocumentContextTest {

    public DocumentContextTest() {
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
    public void contextValidityTest() throws Exception {
        Document document = TestUtil.getResourceAsDocument("api/configmodel/resources/mailreader-default.xml");
        DocumentContext context = DocumentContext.createContext(document, -1);
        assertNull(context);
        
        context = DocumentContext.createContext(document, 507);
        assertNotNull(context);
    }
}