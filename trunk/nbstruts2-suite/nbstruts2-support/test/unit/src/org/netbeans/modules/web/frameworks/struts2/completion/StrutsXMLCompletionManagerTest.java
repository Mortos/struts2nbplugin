package org.netbeans.modules.web.frameworks.struts2.completion;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsQNames;
import static org.junit.Assert.*;

/**
 *
 * @author Rohan Ranade
 */
public class StrutsXMLCompletionManagerTest {

    public StrutsXMLCompletionManagerTest() {
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
    public void completorFactoryRegistrationTest() {
        StrutsXMLCompletionManager instance = StrutsXMLCompletionManager.getDefault();
        assertCompletionCase(instance, StrutsQNames.BEAN, StrutsAttributes.STATIC, AttributeValueCompletorFactory.class);
        assertCompletionCase(instance, StrutsQNames.BEAN, StrutsAttributes.OPTIONAL, AttributeValueCompletorFactory.class);
        assertCompletionCase(instance, StrutsQNames.PACKAGE, StrutsAttributes.ABSTRACT, AttributeValueCompletorFactory.class);
        assertCompletionCase(instance, StrutsQNames.BEAN, StrutsAttributes.SCOPE, AttributeValueCompletorFactory.class);
    }

    private void assertCompletionCase(StrutsXMLCompletionManager mgr, StrutsQNames tag, 
            StrutsAttributes attrib, Class<? extends CompletorFactory> expFactoryClass) {
        CompletorFactory factory = mgr.locateCompletorFactory(tag.getLocalName(), attrib.getName());
        assertEquals(expFactoryClass, factory.getClass());
    }
}