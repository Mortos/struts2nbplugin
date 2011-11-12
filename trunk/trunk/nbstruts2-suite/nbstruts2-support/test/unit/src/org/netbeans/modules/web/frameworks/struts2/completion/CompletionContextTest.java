package org.netbeans.modules.web.frameworks.struts2.completion;

import org.junit.Test;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import org.netbeans.modules.web.frameworks.struts2.completion.CompletionContext.CompletionType;
import static org.junit.Assert.*;

/**
 * The following tests are copied directly from Spring support. Though these tests
 * create spring config documents, they test the behaviour of CompletionContext 
 * correctly
 * 
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public class CompletionContextTest {

    @Test
    public void attributeValueCompletionTest() throws Exception {
        String config = TestUtil.createXMLConfigText("<bean id='petStore' " +
                "class='org.springframework.PetStoreImpl'/>");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(config);
        CompletionContext ctx = new CompletionContextImpl(doc,
                config.indexOf("'petStore'"));
        assertContext(ctx, CompletionType.NONE, "", "bean");
        ctx = new CompletionContextImpl(doc, config.indexOf("petStore"));
        assertContext(ctx, CompletionType.ATTRIBUTE_VALUE, "", "bean");
        ctx = new CompletionContextImpl(doc, config.indexOf("Store"));
        assertContext(ctx, CompletionType.ATTRIBUTE_VALUE, "pet", "bean");
    }

    @Test
    public void attributeCompletionTest() throws Exception {
        String config = TestUtil.createXMLConfigText("<bean id='petStore' " +
                "class='org.springframework.PetStoreImpl' p:name ='Sample Petstore'/>");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(config);
        CompletionContext ctx = new CompletionContextImpl(doc, config.indexOf("id='petStore"));
        assertContext(ctx, CompletionType.ATTRIBUTE, "", "bean");
        ctx = new CompletionContextImpl(doc, config.indexOf("id='petStore"));
        assertContext(ctx, CompletionType.ATTRIBUTE, "", "bean");
        ctx = new CompletionContextImpl(doc, config.indexOf("lass='org."));
        assertContext(ctx, CompletionType.ATTRIBUTE, "c", "bean");
    }
    
    @Test
    public void attributeCompletionAtTagEndTest() throws Exception {
        // empty tag
        String config = TestUtil.createXMLConfigText("<bean id='petStore' " +
                "class='org.springframework.PetStoreImpl' p:name ='Sample Petstore' />");
        BaseDocument doc = TestUtil.createStrutsXMLConfigDocument(config);
        CompletionContext ctx = new CompletionContextImpl(doc, config.indexOf("/>"));
        assertContext(ctx, CompletionType.ATTRIBUTE, "", "bean");
        ctx = new CompletionContextImpl(doc, config.indexOf("/>") + 1);
        assertContext(ctx, CompletionType.NONE, "", "bean");
        ctx = new CompletionContextImpl(doc, config.indexOf(" />"));
        assertContext(ctx, CompletionType.NONE, "", "bean");
        
        config = TestUtil.createXMLConfigText("<bean id='petStore' " +
                "class='org.springframework.PetStoreImpl' p:name ='Sample Petstore'/>");
        doc = TestUtil.createStrutsXMLConfigDocument(config);
        ctx = new CompletionContextImpl(doc, config.indexOf("/>"));
        assertContext(ctx, CompletionType.NONE, "", "bean");
        
        // start tag
        config = TestUtil.createXMLConfigText("<bean id='petStore' " +
                "class='org.springframework.PetStoreImpl' p:name ='Sample Petstore' ></bean>");
        doc = TestUtil.createStrutsXMLConfigDocument(config);
        ctx = new CompletionContextImpl(doc, config.indexOf("></bean>"));
        assertContext(ctx, CompletionType.ATTRIBUTE, "", "bean");
        ctx = new CompletionContextImpl(doc, config.indexOf(" ></bean>"));
        assertContext(ctx, CompletionType.NONE, "", "bean");
        
        config = TestUtil.createXMLConfigText("<bean id='petStore' " +
                "class='org.springframework.PetStoreImpl' p:name ='Sample Petstore'></bean>");
        ctx = new CompletionContextImpl(doc, config.indexOf("></bean>"));
        assertContext(ctx, CompletionType.NONE, "", "bean");
    }

    private void assertContext(CompletionContext context, CompletionType expectedType,
            String expectedPrefix, String expectedTag) {
        assertEquals(expectedType, context.getCompletionType());
        assertEquals(expectedPrefix, context.getTypedPrefix());
        if(expectedTag == null) {
            assertNull(context.getTag());
        } else {
            assertEquals(expectedTag, context.getTag().getNodeName());
        }
    }
}
