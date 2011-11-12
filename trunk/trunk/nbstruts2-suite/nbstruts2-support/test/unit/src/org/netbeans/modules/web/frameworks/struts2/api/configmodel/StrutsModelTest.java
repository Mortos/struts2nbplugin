/*
 * StrutsModelTest.java
 * JUnit 4.x based test
 *
 * Created on August 17, 2007, 1:15 AM
 */

package org.netbeans.modules.web.frameworks.struts2.api.configmodel;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Pisl
 */
public class StrutsModelTest {
    
    public StrutsModelTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void basicReadOnlyTest() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/struts-mailreader.xml");
        assertNotNull(model);
        Struts struts = model.getRootComponent();
        assertNotNull(struts);
        List<Constant> constants = struts.getConstants();
        assertEquals("Number of constants ", 3, constants.size());
        Constant constant = constants.get(0);
        assertEquals("struts.action.extension", constant.getName());
        assertEquals("do", constant.getValue());
        List<Include> includes = struts.getIncludes();
        assertEquals("Number of includes ", 2, includes.size());
    }
    
    @Test
    public void basicResultTextReadTest() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/mailreader-support.xml");
        Struts struts = model.getRootComponent();

        // get first pacckage
        List<StrutsPackage> packs = struts.getPackages();
        StrutsPackage pack = packs.get(0);
        assertEquals("mailreader-support", pack.getName());
        
        // get first action
        List<Action> actions = pack.getActions();
        Action action = actions.get(0);
        assertEquals("Tour", action.getName());
        
        // get its result tag and then test for the text
        List<Result> results = action.getResults();
        Result result = results.get(0);
        assertNotNull(result);
        
        assertEquals("/tour.html", result.getResultText());
    }
    
    @Test
    public void elementAdditionTest() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/mailreader-support.xml");
        model = TestUtil.dumpAndReloadModel(model);
        Struts struts = model.getRootComponent();
        assertEquals(3, struts.getPackages().size());

        // 1. Unordered addition
        StrutsPackage newPack = model.getFactory().createStrutsPackage();
        newPack.setName("testPackage");
        try {
            model.startTransaction();
            model.getRootComponent().addPackage(-1, newPack);
        } finally {
            model.endTransaction();
        }
        model.sync();
        assertEquals(4, struts.getPackages().size());
        assertEquals("mailreader-support", struts.getPackages().get(0).getName());
        assertEquals("subscription", struts.getPackages().get(1).getName());
        assertEquals("wildcard", struts.getPackages().get(2).getName());
        assertEquals("testPackage", struts.getPackages().get(3).getName());
        
        // 2. Ordered addition
        StrutsPackage newPack2 = model.getFactory().createStrutsPackage();
        newPack2.setName("testPackage2");
        try {
            model.startTransaction();
            model.getRootComponent().addPackage(2, newPack2);
        } finally {
            model.endTransaction();
        }
        model.sync();
        
        assertEquals(5, struts.getPackages().size());
        assertEquals("mailreader-support", struts.getPackages().get(0).getName());
        assertEquals("subscription", struts.getPackages().get(1).getName());
        assertEquals("testPackage2", struts.getPackages().get(2).getName());
        assertEquals("wildcard", struts.getPackages().get(3).getName());
        assertEquals("testPackage", struts.getPackages().get(4).getName());
    }
    
    @Test
    public void elementRemovalTest() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/mailreader-default.xml");
        model = TestUtil.dumpAndReloadModel(model);
        
        Struts struts = model.getRootComponent();
        InterceptorStack stack = struts.getPackages().get(0).getInterceptors().getInterceptorStacks().get(0);
        
        assertEquals(2, stack.getInterceptorRefs().size());
        
        try {
            model.startTransaction();
            stack.removeInterceptorRef(stack.getInterceptorRefs().get(0));
        } finally {
            model.endTransaction();
        }
        model.sync();
        
        assertEquals(1, stack.getInterceptorRefs().size());
        assertEquals("defaultStack", stack.getInterceptorRefs().get(0).getName());
    }
}
