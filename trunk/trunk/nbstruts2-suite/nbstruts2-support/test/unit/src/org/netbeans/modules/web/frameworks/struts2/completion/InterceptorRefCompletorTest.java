/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.web.frameworks.struts2.completion;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.text.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import static org.junit.Assert.*;
import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;

/**
 *
 * @author Rohan Ranade
 */
public class InterceptorRefCompletorTest {

    public InterceptorRefCompletorTest() {
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
    public void testDoCompletion() throws Exception {
        Document doc = TestUtil.loadResourceDocument("completion/resources/interceptor-comp-test.xml");
        CompletionContext context = new CompletionContextImpl(doc, 860);
        InterceptorRefCompletor completor = new InterceptorRefCompletor();
        
        List<StrutsXMLConfigCompletionItem> result = completor.doCompletion(context);
        Set<String> expectedItems = new HashSet<String>(Arrays.asList( "a", "b", "c", "d", "x", "y", "z" ));
        assertCompletionItemsText(expectedItems, result);
        
        context = new CompletionContextImpl(doc, 1269);
        expectedItems = new HashSet<String>(Arrays.asList( "a", "b", "c", "d", "x", "y", "z", "n" ));
        result = completor.doCompletion(context);
        assertCompletionItemsText(expectedItems, result);
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