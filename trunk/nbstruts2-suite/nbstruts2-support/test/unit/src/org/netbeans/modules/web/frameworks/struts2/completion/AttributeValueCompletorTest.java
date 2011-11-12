package org.netbeans.modules.web.frameworks.struts2.completion;

import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import static org.junit.Assert.*;

/**
 *
 * @author Sujit Nair, Rohan Ranade
 */
public class AttributeValueCompletorTest {

    public AttributeValueCompletorTest() {
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
    public void doCompletionTest() throws Exception {
        String[] itemTextAndDocs = { "foo", "some text",
            "ffa", "some more text",
            "ffaa", "some new text" };
        AttributeValueCompletor completor = new AttributeValueCompletor(itemTextAndDocs);
        String docText = TestUtil.createXMLConfigText("<package name='mailreader-default' " +
                "namespace='/' extends='ff'/>");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(docText);
        CompletionContext ctx = new CompletionContextImpl(doc, docText.indexOf("ff'/>") + 2);
        List<StrutsXMLConfigCompletionItem> items = completor.doCompletion(ctx);
        Set<String> expectedResult = new HashSet<String>();
        expectedResult.add(itemTextAndDocs[2]);
        expectedResult.add(itemTextAndDocs[4]);
        assertCompletionItemsText(expectedResult, items);
        
        ctx = new CompletionContextImpl(doc, docText.indexOf("ff'/>") + 1);
        expectedResult.add(itemTextAndDocs[0]);
        items = completor.doCompletion(ctx);
        assertCompletionItemsText(expectedResult, items);
    }
    
    private void assertCompletionItemsText(Set<String> expectedText, 
            List<StrutsXMLConfigCompletionItem> actualResult) {
        assertEquals(expectedText.size(), actualResult.size());
        for(StrutsXMLConfigCompletionItem item : actualResult) {
            String s = item.getInsertPrefix().toString();
            assertTrue(expectedText.contains(s));
        }
    }
}